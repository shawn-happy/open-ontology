`GET /api/v2/aipAgents/agents/{agentRid}/agentVersions/{agentVersionString}`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Get version details for an Agent.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:aip-agents-read`.

**OAuth2 scopes**: `api:aip-agents-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `agentRid` | string | 是 | An RID identifying an Agent created in [AIP Chatbot Studio](/docs/foundry/chatbot-studio/overview/).<br>示例: `ri.aip-agents..agent.732cd5b4-7ca7-4219-aabb-6e976faf63b1` |
| `agentVersionString` | string | 是 | The semantic version of the Agent, formatted as "majorVersion.minorVersion".<br>示例: `1.0` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**AgentVersion**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `AgentVersion` | object | 是 | 示例: `{"string":"1.0","version":{"major":1,"minor":2}}` |
| `AgentVersion.string` | string | 是 | The semantic version of the Agent, formatted as "majorVersion.minorVersion".<br>示例: `1.0` |
| `AgentVersion.version` | object | 是 | Semantic version details of the Agent.<br>示例: `{"major":1,"minor":2}` |
| `AgentVersion.version.major` | integer | 是 | The major version of the Agent. Incremented every time the Agent is published.<br>示例: `1` |
| `AgentVersion.version.minor` | integer | 是 | The minor version of the Agent. Incremented every time the Agent is saved.<br>示例: `2` |

```json
{
  "string": "1.0",
  "version": {
    "major": 1,
    "minor": 2
  }
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `InvalidAgentVersion` | The provided version string is not a valid format for an Agent version. |
| INVALID_ARGUMENT | `NoPublishedAgentVersion` | Failed to retrieve the latest published version of the Agent because the Agent has no published versions.<br>Try publishing the Agent in AIP Chatbot Studio to use the latest published version, or specify the version of the Agent to use. |
| NOT_FOUND | `AgentVersionNotFound` | The given AgentVersion could not be found. |
| NOT_FOUND | `AgentNotFound` | The given Agent could not be found. |
