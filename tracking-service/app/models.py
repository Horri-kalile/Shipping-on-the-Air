from pydantic import BaseModel


class TrackingResponse(BaseModel):
    delivery_id: str
    status: str
