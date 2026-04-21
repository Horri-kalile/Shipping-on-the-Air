from motor.motor_asyncio import AsyncIOMotorDatabase

from app.config import MONGO_COLLECTION_NAME
from app.models import TrackingState


class TrackingRepository:
    def __init__(self, database: AsyncIOMotorDatabase):
        self.collection = database[MONGO_COLLECTION_NAME]

    async def ensure_indexes(self) -> None:
        await self.collection.create_index("delivery_id", unique=True)

    async def get_by_delivery_id(self, delivery_id: str) -> TrackingState | None:
        document = await self.collection.find_one({"delivery_id": delivery_id}, {"_id": 0})
        if document is None:
            return None
        return TrackingState(**document)

    async def save(self, tracking_state: TrackingState) -> None:
        await self.collection.update_one(
            {"delivery_id": tracking_state.delivery_id},
            {"$set": tracking_state.model_dump()},
            upsert=True,
        )
