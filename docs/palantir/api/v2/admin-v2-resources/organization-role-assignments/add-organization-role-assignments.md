`POST /api/v2/admin/organizations/{organizationRid}/roleAssignments/add`

Assign roles to principals for the given Organization. At most 100 role assignments can be added in a single request.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-write`.

**OAuth2 scopes**: `api:admin-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `organizationRid` | string | 是 | 示例: `ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa` |

## Request body

```json
{
  "roleAssignments": [
    {
      "roleId": "8bf49052-dc37-4528-8bf0-b551cfb71268",
      "principalId": "f05f8da4-b84c-4fca-9c77-8af0b13d11de"
    }
  ]
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `PrincipalNotFound` | A principal (User or Group) with the given PrincipalId could not be found |
| PERMISSION_DENIED | `AddOrganizationRoleAssignmentsPermissionDenied` | Could not add the OrganizationRoleAssignment. |
| NOT_FOUND | `OrganizationNotFound` | The given Organization could not be found. |
