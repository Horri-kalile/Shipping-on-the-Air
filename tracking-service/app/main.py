from contextlib import asynccontextmanager

from fastapi import FastAPI
from motor.motor_asyncio import AsyncIOMotorClient

from app.api import router
from app.config import APP_NAME, MONGO_DB_NAME, MONGO_URI
from app.repository import TrackingRepository


@asynccontextmanager
async def lifespan(app: FastAPI):
    mongo_client = AsyncIOMotorClient(MONGO_URI)
    database = mongo_client[MONGO_DB_NAME]
    repository = TrackingRepository(database)

    await repository.ensure_indexes()
    app.state.tracking_repository = repository

    try:
        yield
    finally:
        mongo_client.close()


app = FastAPI(title=APP_NAME, lifespan=lifespan)
app.include_router(router)
