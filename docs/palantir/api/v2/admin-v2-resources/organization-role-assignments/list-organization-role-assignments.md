`GET /api/v2/admin/organizations/{organizationRid}/roleAssignments`

List all principals who are assigned a role for the given Organization.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-read`.

**OAuth2 scopes**: `api:admin-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `organizationRid` | string | 是 | 示例: `ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa` |

## Response

**ListOrganizationRoleAssignmentsResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListOrganizationRoleAssignmentsResponse` | object | 是 | 示例: `{"data":[{"roleId":"8bf49052-dc37-4528-8bf0-b551cfb71268","principalId":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","principalType":"USER"}]}` |
| `ListOrganizationRoleAssignmentsResponse.data` | list<OrganizationRoleAssignment> | 否 | — |
| `ListOrganizationRoleAssignmentsResponse.data.OrganizationRoleAssignment` | object | 是 | — |
| `ListOrganizationRoleAssignmentsResponse.data.OrganizationRoleAssignment.principalType` | enum | 是 | 示例: `USER` |
| `ListOrganizationRoleAssignmentsResponse.data.OrganizationRoleAssignment.principalId` | string | 是 | The ID of a Foundry Group or User.<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `ListOrganizationRoleAssignmentsResponse.data.OrganizationRoleAssignment.roleId` | string | 是 | The unique ID for a Role. Roles are sets of permissions that grant different levels of access to resources.<br>The default roles in Foundry are: Owner, Editor, Viewer, and Discoverer. See more about<br>[roles](/docs/foundry/security/projects-and-roles#roles) in the user documentation.<br>示例: `8bf49052-dc37-4528-8bf0-b551cfb71268` |

```json
{
  "data": [
    {
      "roleId": "8bf49052-dc37-4528-8bf0-b551cfb71268",
      "principalId": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
      "principalType": "USER"
    }
  ]
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `ListOrganizationRoleAssignmentsPermissionDenied` | The provided token does not have permission to list assigned roles for this organization. |
| NOT_FOUND | `OrganizationNotFound` | The given Organization could not be found. |
