`GET /api/v2/admin/organizations/{organizationRid}/listAvailableRoles`

List all roles that can be assigned to a principal for the given Organization.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-read`.

**OAuth2 scopes**: `api:admin-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `organizationRid` | string | 是 | 示例: `ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa` |

## Response

**ListAvailableOrganizationRolesResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListAvailableOrganizationRolesResponse` | object | 是 | 示例: `{"data":[{"roleSetId":"3181190f-f6b8-4649-90ec-64fa2d847204","operations":["compass:read-resource"],"id":"8bf49052-dc37-4528-8bf0-b551cfb71268","type":"ORGANIZATION"}]}` |
| `ListAvailableOrganizationRolesResponse.data` | list<Role> | 否 | — |
| `ListAvailableOrganizationRolesResponse.data.Role` | object | 是 | A set of permissions that can be assigned to a principal for a specific resource type. |
| `ListAvailableOrganizationRolesResponse.data.Role.id` | string | 是 | The unique ID for a Role. Roles are sets of permissions that grant different levels of access to resources.<br>The default roles in Foundry are: Owner, Editor, Viewer, and Discoverer. See more about<br>[roles](/docs/foundry/security/projects-and-roles#roles) in the user documentation.<br>示例: `8bf49052-dc37-4528-8bf0-b551cfb71268` |
| `ListAvailableOrganizationRolesResponse.data.Role.roleSetId` | string | 是 | 示例: `3181190f-f6b8-4649-90ec-64fa2d847204` |
| `ListAvailableOrganizationRolesResponse.data.Role.name` | string | 是 | — |
| `ListAvailableOrganizationRolesResponse.data.Role.description` | string | 是 | — |
| `ListAvailableOrganizationRolesResponse.data.Role.isDefault` | boolean | 是 | Default roles are provided by Palantir and cannot be edited or modified by administrators. |
| `ListAvailableOrganizationRolesResponse.data.Role.type` | enum | 是 | The type of resource that is valid for this role.<br>示例: `ORGANIZATION` |
| `ListAvailableOrganizationRolesResponse.data.Role.operations` | list<Operation> | 否 | The operations that a principal can perform with this role on the assigned resource. |
| `ListAvailableOrganizationRolesResponse.data.Role.operations.Operation` | string | 是 | An operation that can be performed on a resource. Operations are used to define the permissions that a Role has.<br>Operations are typically in the format `service:action`, where `service` is related to the type of resource and `action` is the action being performed. |

```json
{
  "data": [
    {
      "roleSetId": "3181190f-f6b8-4649-90ec-64fa2d847204",
      "operations": [
        "compass:read-resource"
      ],
      "id": "8bf49052-dc37-4528-8bf0-b551cfb71268",
      "type": "ORGANIZATION"
    }
  ]
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `ListAvailableRolesOrganizationPermissionDenied` | Could not listAvailableRoles the Organization. |
| NOT_FOUND | `OrganizationNotFound` | The given Organization could not be found. |
