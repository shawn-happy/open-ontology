`GET /api/v2/admin/groups/{groupId}/groupMembers`

Lists all members (which can be a User or a Group) of a given Group.

This is a paged endpoint. Each page may be smaller or larger than the requested page size. However, 
it is guaranteed that if there are more results available, the `nextPageToken` field will be populated. 
To get the next page, make the same request again, but set the value of the `pageToken` query parameter 
to be value of the `nextPageToken` value of the previous response. If there is no `nextPageToken` field 
in the response, you are on the last page.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-read`.

**OAuth2 scopes**: `api:admin-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `groupId` | string | 是 | A Foundry Group ID.<br>示例: `0d1fe74e-2b70-4a93-9b1a-80070637788b` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `transitive` | boolean | 否 | When true, includes the transitive members of groups contained within this group. For example, say the<br>Group has member Group A, and Group A has member User B. If `transitive=false` only Group A will<br>be returned, but if `transitive=true` then Group A and User B will be returned. This<br>will recursively resolve Groups through all layers of nesting.<br>If `transitive` is true, `includeExpirations` cannot also be set to true.<br>Defaults to false. |
| `includeExpirations` | boolean | 否 | When true, includes the expiration time of any temporary members of this group. `includeExpirations`<br>cannot be set to true if `transitive` is also set to true.<br>Defaults to false. |
| `pageSize` | integer | 否 | The page size to use for the endpoint. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

## Response

**ListGroupMembersResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListGroupMembersResponse` | object | 是 | 示例: `{"data":[{"principalId":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","expiration":"2026-01-31T00:00:00.000Z","principalType":"USER"}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListGroupMembersResponse.data` | list<GroupMember> | 否 | — |
| `ListGroupMembersResponse.data.GroupMember` | object | 是 | — |
| `ListGroupMembersResponse.data.GroupMember.principalType` | enum | 是 | 示例: `USER` |
| `ListGroupMembersResponse.data.GroupMember.principalId` | string | 是 | The ID of a Foundry Group or User.<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `ListGroupMembersResponse.data.GroupMember.expiration` | string | 否 | The time at which this member's membership in the group will expire. This field will always be<br>empty unless the `includeExpirations` query parameter is set to true in the list operation.<br>示例: `2026-01-31T00:00:00.000Z` |
| `ListGroupMembersResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    {
      "principalId": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
      "expiration": "2026-01-31T00:00:00.000Z",
      "principalType": "USER"
    }
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `InvalidPageSize` | The provided page size was zero or negative. Page sizes must be greater than zero. |
| INVALID_ARGUMENT | `ExpirationForTransitiveGroupMembersNotSupported` | You cannot pass includeExpirations if transitive is true. |
| PERMISSION_DENIED | `ListGroupMembersPermissionDenied` | The provided token does not have permission to view the members of the given group. |
| NOT_FOUND | `GroupNotFound` | The given Group could not be found. |
