`POST /api/v2/admin/markings/{markingId}/roleAssignments/add`

Adds role assignments for the given Marking. For Organization markings, only the USE and DECLASSIFY
roles are supported; the ADMINISTER role must be managed via the Organization Role Assignment endpoints.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-write`.

**OAuth2 scopes**: `api:admin-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `markingId` | string | 是 | The ID of a security marking.<br>示例: `18212f9a-0e63-4b79-96a0-aae04df23336` |

## Request body

```json
{
  "roleAssignments": [
    {
      "role": "ADMINISTER",
      "principalId": "f05f8da4-b84c-4fca-9c77-8af0b13d11de"
    }
  ]
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `PrincipalNotFound` | A principal (User or Group) with the given PrincipalId could not be found |
| PERMISSION_DENIED | `GetMarkingPermissionDenied` | The provided token does not have permission to view the marking. |
| INVALID_ARGUMENT | `OrganizationMarkingAdministerRoleNotSupported` | The ADMINISTER role on Organization markings cannot be managed through the Marking Role Assignments<br>endpoints. To manage administrator roles for an Organization, use the Organization Role Assignment endpoints<br>instead. |
| PERMISSION_DENIED | `AddMarkingRoleAssignmentsPermissionDenied` | Could not add the MarkingRoleAssignment. |
| NOT_FOUND | `MarkingNotFound` | The given Marking could not be found. |
