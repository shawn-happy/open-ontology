`PUT /api/v2/workbench/boards/{boardRid}/objects/move`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Moves Foundry Objects from their current state to a different state on a Workbench Board.
This operation preserves the objects' board item identifiers and edit history, allowing users
to track the objects' progression through workflow states.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:workbench-write`.

**OAuth2 scopes**: `api:workbench-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `boardRid` | string | 是 | The unique identifier for a Workbench Board<br>示例: `ri.gotham-artifact.0-0.workbench-board.example` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Request body

```json
{
  "objectRids": [
    "ri.phonograph2-objects.main.object.48668bf6-8878-48d2-b8f8-f0017593feb5"
  ],
  "stateId": "3fa85f64-5717-4562-b3fc-2c963f66afa6"
}
```

## Response

**EmptySuccessResponse**

An empty response object indicating the request was successful

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `EmptySuccessResponse` | any | 是 | An empty response object indicating the request was successful |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `ObjectNotOnBoard` | One or more objects are not on the specified board and cannot be moved. |
| NOT_FOUND | `BoardStateNotFound` | The specified state does not exist on the board. |
| INVALID_ARGUMENT | `BoardOperationNotSupported` | The requested operation cannot be performed on this board. |
| PERMISSION_DENIED | `MoveBoardObjectsPermissionDenied` | Could not move the BoardObject. |
