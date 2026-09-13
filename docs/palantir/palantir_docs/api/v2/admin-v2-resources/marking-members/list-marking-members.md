`GET /api/v2/admin/markings/{markingId}/markingMembers`

Lists all principals who can view resources protected by the given Marking. Ignores the `pageSize` parameter.
Requires `api:admin-write` because only marking administrators can view marking members.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-write`.

**OAuth2 scopes**: `api:admin-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `markingId` | string | 是 | The ID of a security marking.<br>示例: `18212f9a-0e63-4b79-96a0-aae04df23336` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `transitive` | boolean | 否 | When true, includes the transitive members of groups contained within groups that are members of this<br>Marking. For example, say the Marking has member Group A, and Group A has member User B. If<br>`transitive=false` only Group A will be returned, but if `transitive=true` then Group A and User B<br>will be returned. This will recursively resolve Groups through all layers of nesting.<br>Defaults to false. |
| `pageSize` | integer | 否 | The page size to use for the endpoint. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

## Response

**ListMarkingMembersResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListMarkingMembersResponse` | object | 是 | 示例: `{"data":[{"principalId":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","principalType":"USER"}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListMarkingMembersResponse.data` | list<MarkingMember> | 否 | — |
| `ListMarkingMembersResponse.data.MarkingMember` | object | 是 | — |
| `ListMarkingMembersResponse.data.MarkingMember.principalType` | enum | 是 | 示例: `USER` |
| `ListMarkingMembersResponse.data.MarkingMember.principalId` | string | 是 | The ID of a Foundry Group or User.<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `ListMarkingMembersResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    {
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
| PERMISSION_DENIED | `ListMarkingMembersPermissionDenied` | The provided token does not have permission to list the members of this marking. |
| PERMISSION_DENIED | `GetMarkingPermissionDenied` | The provided token does not have permission to view the marking. |
| NOT_FOUND | `MarkingNotFound` | The given Marking could not be found. |
