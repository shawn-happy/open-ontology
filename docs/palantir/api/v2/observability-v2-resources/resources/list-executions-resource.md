`POST /api/v2/observability/resources/{resourceRid}/listExecutions`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

List recent executions (function runs, automation runs, etc.) for a given resource.
Returns execution summaries ordered by most recent first.
Only completed executions are included in the results.

If neither `startTime` nor `endTime` is specified, executions from the last 24 hours
are returned.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:observability-read`.

**OAuth2 scopes**: `api:observability-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `resourceRid` | string | 是 | The Resource Identifier (RID) of the Foundry resource.<br>示例: `ri.function-registry.main.function.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Request body

```json
{
  "pageSize": 100,
  "pageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Response

**ListExecutionsResponse**

A page of execution results.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListExecutionsResponse` | object | 是 | A page of execution results.<br>示例: `{"data":[{"duration":{"unit":"SECONDS","value":30},"foundryTraceId":"1f168a0f-8f5a-648c-8ce4-21d9f1ce17c2","resourceVersion":"1.2.0","failureReason":"timeout","traceOwningRid":"ri.object-sentinel.main.monitor.f7c14421-54f4-4e9f-b6f2-1971b79d612b","resourceRid":"ri.function-registry.main.function.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","startTime":"2024-09-25T17:29:35.974Z","callerRid":"ri.workshop.main.module.f1e2d3c4-b5a6-7890-1234-567890abcdef","status":"SUCCEEDED"}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListExecutionsResponse.data` | list<Execution> | 否 | The list of executions matching the query. |
| `ListExecutionsResponse.data.Execution` | object | 是 | A single run of a Foundry compute resource (e.g., one function invocation,<br>one automation run). |
| `ListExecutionsResponse.data.Execution.traceOwningRid` | string | 是 | The RID of the resource that owns the trace context for this execution. Pair with<br>`foundryTraceId` to retrieve logs for the execution.<br>示例: `ri.object-sentinel.main.monitor.f7c14421-54f4-4e9f-b6f2-1971b79d612b` |
| `ListExecutionsResponse.data.Execution.foundryTraceId` | string | 是 | The Foundry trace ID for this execution. Uniquely identifies the trace, but on its<br>own is not yet sufficient to look up logs â pair with `traceOwningRid` to retrieve<br>logs for the execution.<br>示例: `1f168a0f-8f5a-648c-8ce4-21d9f1ce17c2` |
| `ListExecutionsResponse.data.Execution.resourceRid` | string | 是 | The Resource Identifier (RID) of the resource that produced this execution.<br>示例: `ri.function-registry.main.function.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `ListExecutionsResponse.data.Execution.resourceVersion` | string | 是 | The version of the resource at the time of execution.<br>示例: `1.2.0` |
| `ListExecutionsResponse.data.Execution.status` | enum | 是 | Whether the execution succeeded or failed.<br>示例: `SUCCEEDED` |
| `ListExecutionsResponse.data.Execution.failureReason` | string | 否 | An enumerated reason for why the execution failed, if applicable.<br>This is a classification string (e.g., "invalid_parameter", "timeout"), not free-text.<br>示例: `timeout` |
| `ListExecutionsResponse.data.Execution.startTime` | string | 是 | The time at which the execution started.<br>示例: `2024-09-25T17:29:35.974Z` |
| `ListExecutionsResponse.data.Execution.duration` | object | 是 | The total duration of the execution.<br>示例: `{"unit":"SECONDS","value":30}` |
| `ListExecutionsResponse.data.Execution.duration.value` | integer | 是 | The duration value.<br>示例: `30` |
| `ListExecutionsResponse.data.Execution.duration.unit` | enum | 是 | The unit of duration.<br>示例: `SECONDS` |
| `ListExecutionsResponse.data.Execution.callerRid` | string | 否 | The RID of the caller that triggered the execution (e.g., the Workshop app<br>or automation that initiated it).<br>示例: `ri.workshop.main.module.f1e2d3c4-b5a6-7890-1234-567890abcdef` |
| `ListExecutionsResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    {
      "duration": {
        "unit": "SECONDS",
        "value": 30
      },
      "foundryTraceId": "1f168a0f-8f5a-648c-8ce4-21d9f1ce17c2",
      "resourceVersion": "1.2.0",
      "failureReason": "timeout",
      "traceOwningRid": "ri.object-sentinel.main.monitor.f7c14421-54f4-4e9f-b6f2-1971b79d612b",
      "resourceRid": "ri.function-registry.main.function.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da",
      "startTime": "2024-09-25T17:29:35.974Z",
      "callerRid": "ri.workshop.main.module.f1e2d3c4-b5a6-7890-1234-567890abcdef",
      "status": "SUCCEEDED"
    }
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `InvalidExecutionFilter` | The provided execution filter is invalid. This may be due to missing required fields<br>or other malformed filter parameters. |
| PERMISSION_DENIED | `ListExecutionsPermissionDenied` | The provided token does not have permission to list executions for this resource. |
| PERMISSION_DENIED | `ListExecutionsResourcePermissionDenied` | Could not listExecutions the Resource. |
| NOT_FOUND | `ResourceNotFound` | The given Resource could not be found. |
