import asyncio
import json
import logging
from datetime import datetime

from aiokafka import AIOKafkaConsumer

from app.models import TrackingState
from app.repository import TrackingRepository


logger = logging.getLogger(__name__)


class TrackingConsumer:
    def __init__(
        self,
        repository: TrackingRepository,
        bootstrap_servers: str,
        topic: str,
        group_id: str,
    ):
        self.repository = repository
        self.bootstrap_servers = bootstrap_servers
        self.topic = topic
        self.group_id = group_id
        self.consumer: AIOKafkaConsumer | None = None
        self.consumer_task: asyncio.Task | None = None

    async def start(self) -> None:
        self.consumer = AIOKafkaConsumer(
            self.topic,
            bootstrap_servers=self.bootstrap_servers,
            group_id=self.group_id,
            auto_offset_reset="latest",
            value_deserializer=lambda value: json.loads(value.decode("utf-8")),
        )
        await self.consumer.start()
        self.consumer_task = asyncio.create_task(self._consume())

    async def stop(self) -> None:
        if self.consumer_task is not None:
            self.consumer_task.cancel()
            try:
                await self.consumer_task
            except asyncio.CancelledError:
                pass
            self.consumer_task = None

        if self.consumer is not None:
            await self.consumer.stop()
            self.consumer = None

    async def _consume(self) -> None:
        if self.consumer is None:
            return

        async for message in self.consumer:
            await self.handle_event(message.value)

    async def handle_event(self, event: dict) -> None:
        event_type = event.get("type")

        if event_type == "DroneAssignedEvent":
            await self._handle_assigned(event)
            return

        if event_type == "DroneLocationUpdatedEvent":
            await self._handle_location_updated(event)
            return

        if event_type == "DroneAtPickupEvent":
            await self._handle_at_pickup(event)
            return

        if event_type == "DroneDeliveredEvent":
            await self._handle_delivered(event)
            return

        logger.info("Ignored unsupported event type: %s", event_type)

    async def _handle_assigned(self, event: dict) -> None:
        delivery_id = event.get("deliveryId")
        if delivery_id is None:
            return

        tracking_state = await self._get_or_create_state(delivery_id, "ASSIGNED")
        if tracking_state.status == "DELIVERED":
            return

        tracking_state.drone_id = event.get("droneId")
        tracking_state.status = "ASSIGNED"
        tracking_state.last_update_time = self._parse_occurred_at(event.get("occurredAt"))
        await self.repository.save(tracking_state)

    async def _handle_location_updated(self, event: dict) -> None:
        delivery_id = event.get("deliveryId")
        if delivery_id is None:
            return

        tracking_state = await self._get_or_create_state(delivery_id, "IN_TRANSIT")
        if tracking_state.status == "DELIVERED":
            return

        tracking_state.drone_id = event.get("droneId")
        tracking_state.status = "IN_TRANSIT"
        tracking_state.current_latitude = event.get("latitude")
        tracking_state.current_longitude = event.get("longitude")
        tracking_state.last_update_time = self._parse_occurred_at(event.get("occurredAt"))
        await self.repository.save(tracking_state)

    async def _handle_at_pickup(self, event: dict) -> None:
        delivery_id = event.get("deliveryId")
        if delivery_id is None:
            return

        tracking_state = await self._get_or_create_state(delivery_id, "AT_PICKUP")
        if tracking_state.status == "DELIVERED":
            return

        tracking_state.drone_id = event.get("droneId")
        tracking_state.status = "AT_PICKUP"
        tracking_state.last_update_time = self._parse_occurred_at(event.get("occurredAt"))
        await self.repository.save(tracking_state)

    async def _handle_delivered(self, event: dict) -> None:
        delivery_id = event.get("deliveryId")
        if delivery_id is None:
            return

        tracking_state = await self._get_or_create_state(delivery_id, "DELIVERED")
        tracking_state.drone_id = event.get("droneId")
        tracking_state.status = "DELIVERED"
        tracking_state.last_update_time = self._parse_occurred_at(event.get("occurredAt"))
        await self.repository.save(tracking_state)

    async def _get_or_create_state(self, delivery_id: str, default_status: str) -> TrackingState:
        tracking_state = await self.repository.get_by_delivery_id(delivery_id)
        if tracking_state is not None:
            return tracking_state

        return TrackingState(delivery_id=delivery_id, status=default_status)

    def _parse_occurred_at(self, occurred_at: str | None) -> datetime | None:
        if occurred_at is None:
            return None

        return datetime.fromisoformat(occurred_at.replace("Z", "+00:00"))
