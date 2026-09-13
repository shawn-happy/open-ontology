`POST /api/v2/admin/enrollments/{enrollmentRid}/roleAssignments/add`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Assign roles to principals for the given Enrollment. At most 100 role assignments can be added in a single request.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-write`.

**OAuth2 scopes**: `api:admin-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `enrollmentRid` | string | 是 | 示例: `ri.control-panel.main.customer.466f812b-f974-4478-9d4f-90402cd3def6` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

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
| NOT_FOUND | `EnrollmentRoleNotFound` | One of the provided role IDs was not found. |
| PERMISSION_DENIED | `AddEnrollmentRoleAssignmentsPermissionDenied` | Could not add the EnrollmentRoleAssignment. |
| NOT_FOUND | `EnrollmentNotFound` | The given Enrollment could not be found. |
