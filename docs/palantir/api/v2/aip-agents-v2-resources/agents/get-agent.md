`GET /api/v2/aipAgents/agents/{agentRid}`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Get details for an Agent.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:aip-agents-read`.

**OAuth2 scopes**: `api:aip-agents-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `agentRid` | string | 是 | An RID identifying an Agent created in [AIP Chatbot Studio](/docs/foundry/chatbot-studio/overview/).<br>示例: `ri.aip-agents..agent.732cd5b4-7ca7-4219-aabb-6e976faf63b1` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `version` | string | 否 | The version of the Agent to retrieve. If not specified, the latest published version will be returned. |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**Agent**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Agent` | object | 是 | 示例: `{"metadata":{"displayName":"Supply Chain Support Agent","suggestedPrompts":["What is the status of my order?","How do I track my shipment?"],"description":"An intelligent assistant to help answer questions about supply chain operations.","inputPlaceholder":"Ask about supply chain operations..."},"rid":"ri.aip-agents..agent.732cd5b4-7ca7-4219-aabb-6e976faf63b1","version":"1.0","parameters":{"customerName":{"access":"READ_ONLY","description":"The name of the customer to answer supply chain-related questions for."}}}` |
| `Agent.rid` | string | 是 | An RID identifying an Agent created in [AIP Chatbot Studio](/docs/foundry/chatbot-studio/overview/).<br>示例: `ri.aip-agents..agent.732cd5b4-7ca7-4219-aabb-6e976faf63b1` |
| `Agent.version` | string | 是 | The version of this instance of the Agent. |
| `Agent.metadata` | object | 是 | Metadata for an Agent.<br>示例: `{"displayName":"Supply Chain Support Agent","suggestedPrompts":["What is the status of my order?","How do I track my shipment?"],"description":"An intelligent assistant to help answer questions about supply chain operations.","inputPlaceholder":"Ask about supply chain operations..."}` |
| `Agent.metadata.displayName` | string | 是 | The name of the Agent.<br>示例: `Supply Chain Support Agent` |
| `Agent.metadata.description` | string | 否 | The description for the Agent.<br>示例: `An intelligent assistant to help answer questions about supply chain operations.` |
| `Agent.metadata.inputPlaceholder` | string | 否 | The default text to show as the placeholder input for chats with the Agent.<br>示例: `Ask about supply chain operations...` |
| `Agent.metadata.suggestedPrompts` | list<string> | 否 | Prompts to show to the user as example messages to start a conversation with the Agent.<br>示例: `["What is the status of my order?","How do I track my shipment?"]` |
| `Agent.parameters` | map | 否 | The types and names of variables configured for the Agent in [AIP Chatbot Studio](/docs/foundry/chatbot-studio/overview/) in the [application state](/docs/foundry/chatbot-studio/application-state/).<br>These variables can be used to send custom values in prompts sent to an Agent to customize and control the Agent's behavior. |
| `Agent.parameters.ParameterId` | string | 是 | The unique identifier for a variable configured in the application state of an Agent in [AIP Chatbot Studio](/docs/foundry/chatbot-studio/overview/). |
| `Agent.parameters.Parameter` | object | 是 | A variable configured in the application state of an Agent in [AIP Chatbot Studio](/docs/foundry/chatbot-studio/overview/). |
| `Agent.parameters.Parameter.parameterType` | union | 是 | Details of the types of values accepted and defaults for this variable. |
| `Agent.parameters.Parameter.parameterType.string` | object | 否 | — |
| `Agent.parameters.Parameter.parameterType.string.defaultValue` | string | 否 | The default value to use for this variable.<br>示例: `unknown` |
| `Agent.parameters.Parameter.parameterType.objectSet` | object | 否 | — |
| `Agent.parameters.Parameter.parameterType.objectSet.expectedObjectTypes` | list<ObjectTypeId> | 否 | The types of objects that are expected in ObjectSet values passed for this variable. |
| `Agent.parameters.Parameter.parameterType.objectSet.expectedObjectTypes.ObjectTypeId` | string | 是 | The unique identifier (ID) for an object type. This can be viewed in [Ontology Manager](/docs/foundry/ontology-manager/overview/). |
| `Agent.parameters.Parameter.access` | enum | 是 | The access mode controls how the Agent is able to interact with the variable.<br>示例: `READ_ONLY` |
| `Agent.parameters.Parameter.description` | string | 否 | A description to explain the use of this variable.<br>This description is injected into the Agent's prompt to provide context for when to use the variable.<br>示例: `The name of the customer to answer supply chain-related questions for.` |

```json
{
  "metadata": {
    "displayName": "Supply Chain Support Agent",
    "suggestedPrompts": [
      "What is the status of my order?",
      "How do I track my shipment?"
    ],
    "description": "An intelligent assistant to help answer questions about supply chain operations.",
    "inputPlaceholder": "Ask about supply chain operations..."
  },
  "rid": "ri.aip-agents..agent.732cd5b4-7ca7-4219-aabb-6e976faf63b1",
  "version": "1.0",
  "parameters": {
    "customerName": {
      "access": "READ_ONLY",
      "description": "The name of the customer to answer supply chain-related questions for."
    }
  }
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `NoPublishedAgentVersion` | Failed to retrieve the latest published version of the Agent because the Agent has no published versions.<br>Try publishing the Agent in AIP Chatbot Studio to use the latest published version, or specify the version of the Agent to use. |
| INVALID_ARGUMENT | `InvalidAgentVersion` | The provided version string is not a valid format for an Agent version. |
| NOT_FOUND | `AgentNotFound` | The given Agent could not be found. |
| NOT_FOUND | `AgentVersionNotFound` | The given AgentVersion could not be found. |
