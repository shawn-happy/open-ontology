`GET /api/v2/admin/markings/{markingId}/roleAssignments`

List all principals who are assigned a role for the given Marking. Ignores the `pageSize` parameter.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-read`.

**OAuth2 scopes**: `api:admin-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `markingId` | string | 是 | The ID of a security marking.<br>示例: `18212f9a-0e63-4b79-96a0-aae04df23336` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageSize` | integer | 否 | The page size to use for the endpoint. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

## Response

**ListMarkingRoleAssignmentsResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListMarkingRoleAssignmentsResponse` | object | 是 | 示例: `{"data":[{"role":"ADMINISTER","principalId":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","principalType":"USER"}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListMarkingRoleAssignmentsResponse.data` | list<MarkingRoleAssignment> | 否 | — |
| `ListMarkingRoleAssignmentsResponse.data.MarkingRoleAssignment` | object | 是 | — |
| `ListMarkingRoleAssignmentsResponse.data.MarkingRoleAssignment.principalType` | enum | 是 | 示例: `USER` |
| `ListMarkingRoleAssignmentsResponse.data.MarkingRoleAssignment.principalId` | string | 是 | The ID of a Foundry Group or User.<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `ListMarkingRoleAssignmentsResponse.data.MarkingRoleAssignment.role` | enum | 是 | Represents the operations that a user can perform with regards to a Marking.<br>* ADMINISTER: The user can add and remove members from the Marking, update Marking Role Assignments, and change Marking metadata.<br>* DECLASSIFY: The user can remove the Marking from resources in the platform and stop the propagation of the Marking during a transform.<br>* USE: The user can apply the marking to resources in the platform.<br>示例: `ADMINISTER` |
| `ListMarkingRoleAssignmentsResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    {
      "role": "ADMINISTER",
      "principalId": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
      "principalType": "USER"
    }
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `ListMarkingRoleAssignmentsPermissionDenied` | The provided token does not have permission to list assigned roles for this marking. |
| NOT_FOUND | `MarkingNotFound` | The given Marking could not be found. |
