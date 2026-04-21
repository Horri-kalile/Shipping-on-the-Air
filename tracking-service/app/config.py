import os


APP_NAME = os.getenv("APP_NAME", "tracking-service")
API_HOST = os.getenv("API_HOST", "0.0.0.0")
API_PORT = int(os.getenv("API_PORT", "8080"))

KAFKA_BOOTSTRAP_SERVERS = os.getenv("KAFKA_BOOTSTRAP_SERVERS", "localhost:19092")
KAFKA_TOPIC = os.getenv("KAFKA_TOPIC", "drone-events")
KAFKA_GROUP_ID = os.getenv("KAFKA_GROUP_ID", "tracking-service")

MONGO_URI = os.getenv("MONGO_URI", "mongodb://localhost:27018")
MONGO_DB_NAME = os.getenv("MONGO_DB_NAME", "tracking")
MONGO_COLLECTION_NAME = os.getenv("MONGO_COLLECTION_NAME", "tracking_states")

DELIVERY_SERVICE_BASE_URL = os.getenv("DELIVERY_SERVICE_BASE_URL", "http://localhost:8080")
