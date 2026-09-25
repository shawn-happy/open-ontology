`POST /api/v2/filesystem/projects/{projectRid}/references/remove`

Remove references from the given project


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:filesystem-write`.

**OAuth2 scopes**: `api:filesystem-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `projectRid` | string | 是 | The unique resource identifier (RID) of a Project.<br>示例: `ri.compass.main.folder.01a79a9d-e293-48db-a585-9ffe221536e8` |

## Request body

```json
{
  "resources": [
    "ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da"
  ]
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `InvalidResourceReference` | The resource reference is invalid. This can occur when the resource identifier is malformed,<br>the resource type does not match the reference type, or the resource cannot be added as a reference. |
| INVALID_ARGUMENT | `InvalidProject` | The provided resource identifier does not refer to a valid project. |
| PERMISSION_DENIED | `RemoveProjectResourceReferencesPermissionDenied` | Could not remove the ProjectResourceReference. |
| NOT_FOUND | `ProjectNotFound` | The given Project could not be found. |
| NOT_FOUND | `ResourceNotFound` | The given Resource could not be found. |
