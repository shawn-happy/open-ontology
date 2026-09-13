`GET /api/v2/aipAgents/agents/{agentRid}/agentVersions`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

List all versions for an Agent.
Versions are returned in descending order, by most recent versions first.


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

**ListAgentVersionsResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListAgentVersionsResponse` | object | 是 | 示例: `{"data":[{"string":"1.0","version":{"major":1,"minor":2}}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListAgentVersionsResponse.data` | list<AgentVersion> | 否 | — |
| `ListAgentVersionsResponse.data.AgentVersion` | object | 是 | — |
| `ListAgentVersionsResponse.data.AgentVersion.string` | string | 是 | The semantic version of the Agent, formatted as "majorVersion.minorVersion".<br>示例: `1.0` |
| `ListAgentVersionsResponse.data.AgentVersion.version` | object | 是 | Semantic version details of the Agent.<br>示例: `{"major":1,"minor":2}` |
| `ListAgentVersionsResponse.data.AgentVersion.version.major` | integer | 是 | The major version of the Agent. Incremented every time the Agent is published.<br>示例: `1` |
| `ListAgentVersionsResponse.data.AgentVersion.version.minor` | integer | 是 | The minor version of the Agent. Incremented every time the Agent is saved.<br>示例: `2` |
| `ListAgentVersionsResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    {
      "string": "1.0",
      "version": {
        "major": 1,
        "minor": 2
      }
    }
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `AgentNotFound` | The given Agent could not be found. |
