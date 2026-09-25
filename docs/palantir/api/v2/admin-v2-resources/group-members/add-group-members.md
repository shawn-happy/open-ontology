`POST /api/v2/admin/groups/{groupId}/groupMembers/add`

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-write`.

**OAuth2 scopes**: `api:admin-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `groupId` | string | 是 | A Foundry Group ID.<br>示例: `0d1fe74e-2b70-4a93-9b1a-80070637788b` |

## Request body

```json
{
  "expiration": "2026-01-31T00:00:00.000Z",
  "principalIds": [
    "f05f8da4-b84c-4fca-9c77-8af0b13d11de"
  ]
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `PrincipalNotFound` | A principal (User or Group) with the given PrincipalId could not be found |
| INVALID_ARGUMENT | `InvalidGroupMembershipExpiration` | The member expiration you provided does not conform to the Group's requirements for member expirations. |
| PERMISSION_DENIED | `AddGroupMembersPermissionDenied` | Could not add the GroupMember. |
| NOT_FOUND | `GroupNotFound` | The given Group could not be found. |
