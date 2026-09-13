`POST /api/v2/filesystem/resources/{resourceRid}/restore`

Restore the given resource and any directly trashed ancestors from the trash. If the resource is not
trashed, this operation will be ignored.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:filesystem-write`.

**OAuth2 scopes**: `api:filesystem-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `resourceRid` | string | 是 | The unique resource identifier (RID) of a resource.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `ResourceNotDirectlyTrashed` | The resource is not directly trashed. |
| PERMISSION_DENIED | `RestoreResourcePermissionDenied` | Could not restore the Resource. |
| NOT_FOUND | `ResourceNotFound` | The given Resource could not be found. |
