# Project Aether - API Reference Specification

This file documents the exact payload formats, endpoints, and expected responses utilized within the system architecture.

---

## 1. Monitor Management API

### Add a New Target Monitor
Registers a web service configuration into the system repository layer.

- **URL**: `http://localhost:4000/api/monitor`
- **Method**: `POST`
- **Content-Type**: `application/json`

#### Request Body
```json
{
  "url": "https://www.google.com/",
  "siteName": "Google",
  "region": "US-EAST-1",
  "encryptedToken": "f+fN672KnvwMQchZW/hDayLAmA06S6BO/myEQ267MKx4G0pIruGpHZoJdhBoFykv27qdCFs="
}
```
<ul><li>Note: The encryptedToken field is optional and can be omitted if the target service does not require access token authorization headers.</li></ul>

#### Expected Response (201 Created or 200 OK)
```json
{
"id": "4a5b4243-71b1-4c96-bcea-8f7eacfb4cc7",
"url": "https://www.google.com/",
"siteName": "Google",
"region": "US-EAST-1",
"status": "PENDING",
"encryptedToken": "MMMogWBdXhxJN4ya/ps6viqoIdZyNjh7Hc4pCLETwMWXtVRPssZQ/ubc9ZOp315Zpt2KBhIwcTUP7vgd/mbVDVnvV0JBnw5KA15FqM+ST1A=",
"createdAt": "2026-05-16T23:11:52.200Z"
}
```

### Get All Registered Monitors
Retrieves the complete list of target monitors loaded within the system state.

- **URL**: `http://localhost:4000/api/monitor`
- **Method**: `GET`
- **Accept**: `application/json`

#### Expected Response (200 OK)
```json
[
  {
    "id": "4a5b4243-71b1-4c96-bcea-8f7eacfb4cc7",
    "url": "https://www.youtube.com/",
    "siteName": "Youtube",
    "region": "US-EAST-1",
    "status": "UP ✅",
    "encryptedToken": "f+fN672KnvwMQchZW/hDayLAmA06S6BO/myEQ267MKx4G0pIruGpHZoJdhBoFykv27qdCFs=",
    "createdAt": "2026-05-15T19:45:59.285717"
  },
  {
    "id": "4a5b4243-71b1-4c96-bcea-8f7eacfb4cc7",
    "url": "https://www.google.com/",
    "siteName": "Google",
    "region": "US-EAST-1",
    "status": "UP ✅",
    "encryptedToken": "MMMogWBdXhxJN4ya/ps6viqoIdZyNjh7Hc4pCLETwMWXtVRPssZQ/ubc9ZOp315Zpt2KBhIwcTUP7vgd/mbVDVnvV0JBnw5KA15FqM+ST1A=",
    "createdAt": "2026-05-16T23:11:52.200Z"
  }
]
```

## 2.Telemetry Streaming API
Establishes a persistent execution pipe to stream continuous real-time diagnostic observations from the central service broadcaster directly to the user dashboard.

- **URL**: `http://localhost:4000/api/v1/logs/stream`
- **Method**: `GET`
- **Content-Type**: `text/event-stream`

### Event Stream Delivery Format (text/event-stream)
The server pushes structured individual update fragments bound to the pulse event channels as data objects:
```
event: pulse
data: {"monitorId":"4a5b4243-71b1-4c96-bcea-8f7eacfb4cc7","statusCode":200,"latencyMs":619,"region":"US-EAST-1"}

event: pulse
data: {"monitorId":"2dcc8ee0-74fa-443b-b8a9-6de1402fa870","statusCode":200,"latencyMs":164,"region":"US-EAST-1"}
```