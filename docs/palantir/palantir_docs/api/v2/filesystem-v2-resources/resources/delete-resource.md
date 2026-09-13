`DELETE /api/v2/filesystem/resources/{resourceRid}`

Move the given resource to the trash. Following this operation, the resource can be restored, using the
`restore` operation, or permanently deleted using the `permanentlyDelete` operation.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:filesystem-write`.

**OAuth2 scopes**: `api:filesystem-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `resourceRid` | string | 是 | The unique resource identifier (RID) of a resource.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `TrashingSpaceNotSupported` | Spaces cannot be trashed. |
| INVALID_ARGUMENT | `TrashingAutosavedResourcesNotSupported` | Auto-saved resources cannot be trashed. |
| INVALID_ARGUMENT | `TrashingHiddenResourcesNotSupported` | Hidden resources cannot be trashed. |
| PERMISSION_DENIED | `DeleteResourcePermissionDenied` | Could not delete the Resource. |
| NOT_FOUND | `ResourceNotFound` | The given Resource could not be found. |
