`PUT /api/v2/aipAgents/agents/{agentRid}/sessions/{sessionRid}/updateTitle`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Update the title for a session.
Use this to set a custom title for a session to help identify it in the list of sessions with an Agent.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:aip-agents-write`.

**OAuth2 scopes**: `api:aip-agents-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `agentRid` | string | 是 | An RID identifying an Agent created in [AIP Chatbot Studio](/docs/foundry/chatbot-studio/overview/).<br>示例: `ri.aip-agents..agent.732cd5b4-7ca7-4219-aabb-6e976faf63b1` |
| `sessionRid` | string | 是 | The Resource Identifier (RID) of the conversation session.<br>示例: `ri.aip-agents..session.292db3b2-b653-4de6-971c-7e97a7b881d6` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Request body

```json
{
  "title": "Order status 02/01"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `UpdateSessionTitlePermissionDenied` | Could not updateTitle the Session. |
| NOT_FOUND | `SessionNotFound` | The given Session could not be found. |
| NOT_FOUND | `AgentNotFound` | The given Agent could not be found. |
