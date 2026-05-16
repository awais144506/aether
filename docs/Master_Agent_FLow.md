# Project Aether: Development Log - Neural Link Phase

**Date:** 2026-05-15
**Status:** MVP Core Functionality Verified
**Architecture:** Distributed Master-Agent (Push-Stream Model)

---

## 1. Architectural Overview
The system has been decoupled into two distinct operational layers, separating management from execution. This design ensures that the central orchestrator is never blocked by network I/O.

| Component | Role | Functional Analogy |
| :--- | :--- | :--- |
| Master Node | Control Plane | Orchestrates missions and manages persistent storage. |
| Agent Node | Data Plane | Performs physical network observations at the edge. |
| gRPC Stream | System Bus | Low-latency binary pathway for real-time synchronization. |

---

## 2. Master Node Implementation
The Master serves as a stateful orchestrator, maintaining a real-time representation of the system state in memory.

### Master Registry (In-Memory Working Set)
- Utilizes a `ConcurrentHashMap` to store monitor configurations.
- Provides $O(1)$ lookup times for regional mission dispatching.
- Acts as a high-speed cache to prevent redundant database polling.

### Master Neural Service (The Dispatcher)
- Implements a bi-directional gRPC stream.
- Handshake Logic: Identifies connecting Agents by region and performs an initial synchronization of all relevant missions.
- Delta Updates: Supports real-time pushes of new monitor configurations without requiring a system restart.

### Pulse Service (The Historian)
- Handles incoming `PulseResult` packets from the gRPC stream.
- Updates the live status in the Master Registry.
- Persists time-series data to PostgreSQL/TimescaleDB for historical analysis.

---

## 3. Agent Node Implementation
The Agent is designed as an autonomous worker, capable of maintaining the monitoring pulse even if the connection to the Master is temporarily interrupted.

### Nervous System Client
- Handshake Protocol: Transmits an initial packet containing the `AGENT_REGION` environment variable to register with the Master.
- Local Registry: Maintains a local copy of assigned missions to minimize unnecessary network chatter.

### Autonomous Execution Loop
- Utilizes `@Scheduled` tasks to trigger pulses at fixed intervals.
- Leverages Java 21 Virtual Threads to execute pings in parallel.
- Streams results back to the Master immediately upon completion of each network request.

---

## 4. Data Flow Sequence

1. **Hydration:** At startup, the Master loads the monitor table from the database into the RAM Registry.
2. **Handshake:** The Agent initiates a gRPC connection and identifies its region (e.g., US-EAST-1).
3. **Synchronization:** The Master filters the RAM Registry by region and pushes the mission list to the Agent.
4. **Observation:** The Agent iterates through its local mission list, performing HTTP requests via Virtual Threads.
5. **Feedback:** The Agent streams binary results back to the Master.
6. **Persistence:** The Master updates the live registry and commits the log to the time-series database.

---

## 5. Engineering Efficiency and Logic Alignment
Following the principles of low-level computer architecture (Petzold Logic):
- **Address Decoding:** The `region` field acts as a decoder, routing data packets to the correct physical "output port" (Agent).
- **Instruction Set:** Protobuf serves as the machine language, reducing the payload size significantly compared to text-based protocols like JSON.
- **I/O Offloading:** The Master delegates the high-latency task of network waiting to the Agents, allowing the central CPU to remain available for user requests.

---

## 6. Next Phase Objectives
- Simulation of multiple global regions using Docker Compose.
- Implementation of Server-Sent Events (SSE) for real-time dashboard updates.
- Development of a consensus engine to verify service outages across multiple geographic points.