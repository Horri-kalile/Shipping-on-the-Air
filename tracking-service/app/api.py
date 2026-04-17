from fastapi import APIRouter, HTTPException, Request

from app.models import TrackingResponse


router = APIRouter()


@router.get("/health")
async def health() -> dict[str, str]:
    return {"status": "ok"}


@router.get("/tracking/{delivery_id}", response_model=TrackingResponse)
async def get_tracking(delivery_id: str, request: Request) -> TrackingResponse:
    tracking_state = await request.app.state.tracking_repository.get_by_delivery_id(delivery_id)
    if tracking_state is None:
        raise HTTPException(status_code=404, detail="Tracking not found")

    return TrackingResponse(**tracking_state.model_dump())
