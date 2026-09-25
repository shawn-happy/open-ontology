`POST /api/v2/functions/queries/{queryApiName}/streamingExecute`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Executes a Query and returns results as a Server-Sent Events (`text/event-stream`) stream.
By default, this executes the highest semantic version of the query, excluding pre-release
versions. To resolve the most recently published version instead, including pre-release
versions, set `latestVersionResolution` to `PUBLISH_TIME`.

This endpoint supports all Query functions. Each SSE event's `data` field is a JSON-encoded
`StreamingExecuteQueryResponse` â either a data batch (`type: data`) carrying one or more
result values, or an error (`type: error`) emitted before stream termination if execution
fails. Non-streaming functions emit a single data event containing the entire result;
streaming functions emit a data event per batch as results become available.

Per the Server-Sent Events specification, each event is terminated by a blank line:

```
data: {"type":"data","value":[{"productId":"SKU-001","price":29.99}]}

data: {"type":"error","errorCode":"INVALID_ARGUMENT","errorName":"QueryRuntimeError","errorInstanceId":"3f8a9c7b-2e4d-4a1f-9b8c-7d6e5f4a3b2c","errorDescription":"Division by zero","parameters":{}}

```


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:functions-execute`.

**OAuth2 scopes**: `api:functions-execute`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `queryApiName` | string | 是 | The name of the Query in the API.<br>示例: `myQueryFunction` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `transactionId` | string | 否 | The ID of a transaction to read from. Transactions are an experimental feature and not all workflows may be supported.<br>示例: `transaction-1` |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Request body

```json
{
  "latestVersionResolution": "PUBLISH_TIME",
  "parameters": {
    "price": 29.99
  },
  "version": "1.2.3",
  "branch": "ri.branch..branch.d827184f-ee0e-4351-8b70-efbe51e07252",
  "ontology": "example-ontology"
}
```

## Response

**StreamingExecuteQueryResponse**

Returns a stream of Server-Sent Events (`text/event-stream`). Each event's `data` field is a JSON-encoded payload of the type described below.<br>A single message in a streaming Query execution response. Each message contains either a data batch or an error.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `StreamingExecuteQueryResponse` | union | 是 | Returns a stream of Server-Sent Events (`text/event-stream`). Each event's `data` field is a JSON-encoded payload of the type described below.<br>A single message in a streaming Query execution response. Each message contains either a data batch or an error. |
| `StreamingExecuteQueryResponse.data` | object | 否 | A batch of query results. |
| `StreamingExecuteQueryResponse.data.value` | any | 是 | Represents the value of data in the following format. Note that these values can be nested, for example an array of structs.<br>\| Type                        \| JSON encoding                                         \| Example                                                                       \|<br>\|-----------------------------\|-------------------------------------------------------\|-------------------------------------------------------------------------------\|<br>\| Array                       \| array                                                 \| `["alpha", "bravo", "charlie"]`                                               \|<br>\| Attachment                  \| string                                                \| `"ri.attachments.main.attachment.2f944bae-5851-4204-8615-920c969a9f2e"`       \|<br>\| Boolean                     \| boolean                                               \| `true`                                                                        \|<br>\| Byte                        \| number                                                \| `31`                                                                          \|<br>\| Date                        \| ISO 8601 extended local date string                   \| `"2021-05-01"`                                                                \|<br>\| Decimal                     \| string                                                \| `"2.718281828"`                                                               \|<br>\| Float                       \| number                                                \| `3.14159265`                                                                  \|<br>\| Double                      \| number                                                \| `3.14159265`                                                                  \|<br>\| Integer                     \| number                                                \| `238940`                                                                      \|<br>\| Long                        \| string                                                \| `"58319870951433"`                                                            \|<br>\| Marking                     \| string                                                \| `"MU"`                                                                        \|<br>\| Null                        \| null                                                  \| `null`                                                                        \|<br>\| Set                         \| array                                                 \| `["alpha", "bravo", "charlie"]`                                               \|<br>\| Short                       \| number                                                \| `8739`                                                                        \|<br>\| String                      \| string                                                \| `"Call me Ishmael"`                                                           \|<br>\| Struct                      \| JSON object                                           \| `{"name": "John Doe", "age": 42}`                                             \|<br>\| TwoDimensionalAggregation   \| JSON object                                           \| `{"groups": [{"key": "alpha", "value": 100}, {"key": "beta", "value": 101}]}` \|<br>\| ThreeDimensionalAggregation \| JSON object                                           \| `{"groups": [{"key": "NYC", "groups": [{"key": "Engineer", "value" : 100}]}]}`\|<br>\| Timestamp                   \| ISO 8601 extended offset date-time string in UTC zone \| `"2021-01-04T05:00:00Z"`                                                      \|<br>示例: `29.99` |
| `StreamingExecuteQueryResponse.error` | object | 否 | An error that occurred during query execution. |
| `StreamingExecuteQueryResponse.error.errorCode` | string | 是 | — |
| `StreamingExecuteQueryResponse.error.errorName` | string | 是 | — |
| `StreamingExecuteQueryResponse.error.errorInstanceId` | string | 是 | — |
| `StreamingExecuteQueryResponse.error.errorDescription` | string | 否 | — |
| `StreamingExecuteQueryResponse.error.parameters` | map | 否 | — |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `StreamingExecuteQueryPermissionDenied` | Could not streamingExecute the Query. |
