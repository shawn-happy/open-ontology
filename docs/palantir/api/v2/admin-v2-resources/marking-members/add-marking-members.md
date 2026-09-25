`POST /api/v2/admin/markings/{markingId}/markingMembers/add`

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-write`.

**OAuth2 scopes**: `api:admin-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `markingId` | string | 是 | The ID of a security marking.<br>示例: `18212f9a-0e63-4b79-96a0-aae04df23336` |

## Request body

```json
{
  "principalIds": [
    "f05f8da4-b84c-4fca-9c77-8af0b13d11de"
  ]
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `PrincipalNotFound` | A principal (User or Group) with the given PrincipalId could not be found |
| PERMISSION_DENIED | `GetMarkingPermissionDenied` | The provided token does not have permission to view the marking. |
| PERMISSION_DENIED | `AddMarkingMembersPermissionDenied` | Could not add the MarkingMember. |
| NOT_FOUND | `MarkingNotFound` | The given Marking could not be found. |
