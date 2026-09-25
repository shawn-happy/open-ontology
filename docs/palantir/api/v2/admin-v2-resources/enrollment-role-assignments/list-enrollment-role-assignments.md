`GET /api/v2/admin/enrollments/{enrollmentRid}/roleAssignments`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

List all principals who are assigned a role for the given Enrollment.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-read`.

**OAuth2 scopes**: `api:admin-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `enrollmentRid` | string | 是 | 示例: `ri.control-panel.main.customer.466f812b-f974-4478-9d4f-90402cd3def6` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**ListEnrollmentRoleAssignmentsResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListEnrollmentRoleAssignmentsResponse` | object | 是 | 示例: `{"data":[{"roleId":"8bf49052-dc37-4528-8bf0-b551cfb71268","principalId":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","principalType":"USER"}]}` |
| `ListEnrollmentRoleAssignmentsResponse.data` | list<EnrollmentRoleAssignment> | 否 | — |
| `ListEnrollmentRoleAssignmentsResponse.data.EnrollmentRoleAssignment` | object | 是 | — |
| `ListEnrollmentRoleAssignmentsResponse.data.EnrollmentRoleAssignment.principalType` | enum | 是 | 示例: `USER` |
| `ListEnrollmentRoleAssignmentsResponse.data.EnrollmentRoleAssignment.principalId` | string | 是 | The ID of a Foundry Group or User.<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `ListEnrollmentRoleAssignmentsResponse.data.EnrollmentRoleAssignment.roleId` | string | 是 | The unique ID for a Role. Roles are sets of permissions that grant different levels of access to resources.<br>The default roles in Foundry are: Owner, Editor, Viewer, and Discoverer. See more about<br>[roles](/docs/foundry/security/projects-and-roles#roles) in the user documentation.<br>示例: `8bf49052-dc37-4528-8bf0-b551cfb71268` |

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
| PERMISSION_DENIED | `ListEnrollmentRoleAssignmentsPermissionDenied` | The provided token does not have permission to list assigned roles for this enrollment. |
| NOT_FOUND | `EnrollmentNotFound` | The given Enrollment could not be found. |
