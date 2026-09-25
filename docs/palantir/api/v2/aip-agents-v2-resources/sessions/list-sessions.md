`GET /api/v2/aipAgents/agents/{agentRid}/sessions`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

List all conversation sessions between the calling user and an Agent that was created by this client.
This does not list sessions for the user created by other clients.
For example, any sessions created by the user in AIP Chatbot Studio will not be listed here.
Sessions are returned in order of most recently updated first.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:aip-agents-read`.

**OAuth2 scopes**: `api:aip-agents-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `agentRid` | string | 是 | An RID identifying an Agent created in [AIP Chatbot Studio](/docs/foundry/chatbot-studio/overview/).<br>示例: `ri.aip-agents..agent.732cd5b4-7ca7-4219-aabb-6e976faf63b1` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageSize` | integer | 否 | The page size to use for the endpoint. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**ListSessionsResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListSessionsResponse` | object | 是 | 示例: `{"data":[{"metadata":{"updatedTime":"2024-10-01T22:04:24.962583055Z","estimatedExpiresTime":"2024-10-02T22:04:24.962583055Z","messageCount":6,"createdTime":"2024-10-01T20:04:24.962583055Z","title":"What is the status of my order?"},"agentRid":"ri.aip-agents..agent.732cd5b4-7ca7-4219-aabb-6e976faf63b1","agentVersion":"1.0","rid":"ri.aip-agents..session.292db3b2-b653-4de6-971c-7e97a7b881d6"}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListSessionsResponse.data` | list<Session> | 否 | — |
| `ListSessionsResponse.data.Session` | object | 是 | — |
| `ListSessionsResponse.data.Session.rid` | string | 是 | The Resource Identifier (RID) of the conversation session.<br>示例: `ri.aip-agents..session.292db3b2-b653-4de6-971c-7e97a7b881d6` |
| `ListSessionsResponse.data.Session.metadata` | object | 是 | Metadata about the session.<br>示例: `{"updatedTime":"2024-10-01T22:04:24.962583055Z","estimatedExpiresTime":"2024-10-02T22:04:24.962583055Z","messageCount":6,"createdTime":"2024-10-01T20:04:24.962583055Z","title":"What is the status of my order?"}` |
| `ListSessionsResponse.data.Session.metadata.title` | string | 是 | The title of the session.<br>示例: `What is the status of my order?` |
| `ListSessionsResponse.data.Session.metadata.createdTime` | string | 是 | The time the session was created.<br>示例: `2024-10-01T20:04:24.962583055Z` |
| `ListSessionsResponse.data.Session.metadata.updatedTime` | string | 是 | The time the session was last updated.<br>示例: `2024-10-01T22:04:24.962583055Z` |
| `ListSessionsResponse.data.Session.metadata.messageCount` | integer | 是 | The count of messages in the session.<br>Includes both user messages and Agent replies, so each complete exchange counts as two messages.<br>示例: `6` |
| `ListSessionsResponse.data.Session.metadata.estimatedExpiresTime` | string | 是 | The estimated time at which the session is due to expire.<br>Once a session has expired, it can no longer be accessed and a new session must be created.<br>The expiry time is automatically extended when new exchanges are added to the session.<br>示例: `2024-10-02T22:04:24.962583055Z` |
| `ListSessionsResponse.data.Session.agentRid` | string | 是 | The Resource Identifier (RID) of the Agent associated with the session. |
| `ListSessionsResponse.data.Session.agentVersion` | string | 是 | The version of the Agent associated with the session.<br>This can be set by clients on session creation.<br>If not specified, defaults to use the latest published version of the Agent at session creation time. |
| `ListSessionsResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    {
      "metadata": {
        "updatedTime": "2024-10-01T22:04:24.962583055Z",
        "estimatedExpiresTime": "2024-10-02T22:04:24.962583055Z",
        "messageCount": 6,
        "createdTime": "2024-10-01T20:04:24.962583055Z",
        "title": "What is the status of my order?"
      },
      "agentRid": "ri.aip-agents..agent.732cd5b4-7ca7-4219-aabb-6e976faf63b1",
      "agentVersion": "1.0",
      "rid": "ri.aip-agents..session.292db3b2-b653-4de6-971c-7e97a7b881d6"
    }
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `AgentNotFound` | The given Agent could not be found. |
