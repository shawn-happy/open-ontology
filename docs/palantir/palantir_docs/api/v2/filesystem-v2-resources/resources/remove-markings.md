`POST /api/v2/filesystem/resources/{resourceRid}/removeMarkings`

Removes Markings from a resource.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:filesystem-write`.

**OAuth2 scopes**: `api:filesystem-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `resourceRid` | string | 是 | The unique resource identifier (RID) of a resource.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |

## Request body

```json
{
  "markingIds": [
    "18212f9a-0e63-4b79-96a0-aae04df23336"
  ]
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `OrganizationMarkingNotSupported` | Adding an organization marking as a regular marking is not supported. Use the organization endpoints on a<br>project resource instead. |
| INVALID_ARGUMENT | `ForbiddenOperationOnHiddenResource` | Performing this operation on a hidden resource is not supported. |
| INVALID_ARGUMENT | `ForbiddenOperationOnAutosavedResource` | Performing this operation on an autosaved resource is not supported. |
| NOT_FOUND | `MarkingNotFound` | A provided marking ID cannot be found. |
| PERMISSION_DENIED | `RemoveMarkingsPermissionDenied` | Could not removeMarkings the Resource. |
| NOT_FOUND | `ResourceNotFound` | The given Resource could not be found. |
