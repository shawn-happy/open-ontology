`POST /api/v2/filesystem/resources/{resourceRid}/roles/remove`

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:filesystem-write`.

**OAuth2 scopes**: `api:filesystem-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `resourceRid` | string | 是 | The unique resource identifier (RID) of a resource.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |

## Request body

```json
{
  "roles": [
    {
      "resourceRolePrincipal": {
        "type": "principalIdOnly",
        "principalId": "f05f8da4-b84c-4fca-9c77-8af0b13d11de"
      },
      "roleId": "8bf49052-dc37-4528-8bf0-b551cfb71268"
    }
  ]
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `InvalidRoleIds` | A roleId referenced in either default roles or role grants does not exist in the project role set for the space. |
| NOT_FOUND | `PrincipalNotFound` | A principal (User or Group) with the given PrincipalId could not be found |
| PERMISSION_DENIED | `RemoveResourceRolesPermissionDenied` | Could not remove the ResourceRole. |
| NOT_FOUND | `ResourceNotFound` | The given Resource could not be found. |
