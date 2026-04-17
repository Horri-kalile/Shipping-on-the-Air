from datetime import datetime

from pydantic import BaseModel


class TrackingState(BaseModel):
    delivery_id: str
    drone_id: str | None = None
    status: str
    current_latitude: float | None = None
    current_longitude: float | None = None
    last_update_time: datetime | None = None
    estimated_remaining_minutes: int | None = None


class TrackingResponse(BaseModel):
    delivery_id: str
    drone_id: str | None = None
    status: str
    current_latitude: float | None = None
    current_longitude: float | None = None
    last_update_time: datetime | None = None
    estimated_remaining_minutes: int | None = None
