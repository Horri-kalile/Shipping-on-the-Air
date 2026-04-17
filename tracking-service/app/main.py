from contextlib import asynccontextmanager

from fastapi import FastAPI
from motor.motor_asyncio import AsyncIOMotorClient

from app.api import router
from app.config import (
    APP_NAME,
    KAFKA_BOOTSTRAP_SERVERS,
    KAFKA_GROUP_ID,
    KAFKA_TOPIC,
    MONGO_DB_NAME,
    MONGO_URI,
)
from app.consumer import TrackingConsumer
from app.repository import TrackingRepository


@asynccontextmanager
async def lifespan(app: FastAPI):
    mongo_client = AsyncIOMotorClient(MONGO_URI)
    database = mongo_client[MONGO_DB_NAME]
    repository = TrackingRepository(database)
    consumer = TrackingConsumer(
        repository=repository,
        bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
        topic=KAFKA_TOPIC,
        group_id=KAFKA_GROUP_ID,
    )

    await repository.ensure_indexes()
    await consumer.start()
    app.state.tracking_repository = repository
    app.state.tracking_consumer = consumer

    try:
        yield
    finally:
        await consumer.stop()
        mongo_client.close()


app = FastAPI(title=APP_NAME, lifespan=lifespan)
app.include_router(router)
