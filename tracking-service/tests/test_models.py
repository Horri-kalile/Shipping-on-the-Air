import pytest
from app.models import TrackingState

def test_tracking_state_serialization():
    """
    Simple test to verify that the TrackingState model can be instantiated.
    """
    state = TrackingState(
        delivery_id="del-123",
        status="PICKED_UP",
        current_latitude=45.0,
        current_longitude=10.0,
        estimated_remaining_minutes=15
    )
    assert state.delivery_id == "del-123"
    assert state.status == "PICKED_UP"
    assert state.current_latitude == 9945.0
