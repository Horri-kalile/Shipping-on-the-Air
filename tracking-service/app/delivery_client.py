import logging

import httpx


logger = logging.getLogger(__name__)


class DeliveryServiceClient:
    def __init__(self, base_url: str):
        self.base_url = base_url.rstrip("/")
        self.client = httpx.AsyncClient(base_url=self.base_url, timeout=5.0)

    async def close(self) -> None:
        await self.client.aclose()

    async def get_remaining_minutes(self, delivery_id: str) -> int | None:
        try:
            response = await self.client.get(f"/deliveries/{delivery_id}/remaining-time")
            response.raise_for_status()
        except httpx.HTTPError as error:
            logger.warning(
                "Unable to fetch remaining time for delivery %s: %s",
                delivery_id,
                error,
            )
            return None

        payload = response.json()
        remaining_minutes = payload.get("remainingMinutes")

        if isinstance(remaining_minutes, int):
            return remaining_minutes

        return None
