`PUT /api/v2/workbench/boards/{boardRid}/objects/add`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Adds Foundry Objects to a Workbench Board. This operation links the objects to the board,
allowing them to be tracked within the board's workflow. If no state is specified, the objects
will be placed in the board's default state (first column).


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
| CONFLICT | `ObjectAlreadyOnBoard` | One or more objects are already on the specified board and cannot be added again. |
| NOT_FOUND | `BoardStateNotFound` | The specified state does not exist on the board. |
| INVALID_ARGUMENT | `BoardHasNoDefaultState` | The board has no default state configured and no state was specified in the request. |
| NOT_FOUND | `ObjectNotFound` | One or more Foundry Objects could not be found or accessed. |
| INVALID_ARGUMENT | `ObjectTypeNotSupported` | One or more objects do not implement the interface type or object type required by the board. |
| FAILED_PRECONDITION | `BoardInterfaceTypeNotSet` | The board requires an interface type to be configured before objects can be added. |
| INVALID_ARGUMENT | `ObjectSecurityNotSatisfied` | One or more objects do not satisfy the security requirements of the board. |
| INVALID_ARGUMENT | `BoardExceededItemLimit` | Adding the requested objects would exceed the maximum number of items allowed on the board. |
| INVALID_ARGUMENT | `BoardOperationNotSupported` | The requested operation cannot be performed on this board. |
| PERMISSION_DENIED | `AddBoardObjectsPermissionDenied` | Could not add the BoardObject. |
