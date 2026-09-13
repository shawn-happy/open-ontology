`POST /api/v2/filesystem/resources/{resourceRid}/permanentlyDelete`

Permanently delete the given resource from the trash. If the resource is not directly trashed, a
`ResourceNotTrashed` error will be thrown.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:filesystem-write`.

**OAuth2 scopes**: `api:filesystem-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `resourceRid` | string | 是 | The unique resource identifier (RID) of a resource.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `ResourceNotTrashed` | The resource should be directly trashed before being permanently deleted. |
| PERMISSION_DENIED | `PermanentlyDeleteResourcePermissionDenied` | Could not permanentlyDelete the Resource. |
| NOT_FOUND | `ResourceNotFound` | The given Resource could not be found. |
