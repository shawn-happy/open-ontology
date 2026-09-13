`GET /api/v2/admin/groups`

Lists all Groups.

This is a paged endpoint. Each page may be smaller or larger than the requested page size. However, it is guaranteed that if there are more results available, the `nextPageToken` field will be populated. To get the next page, make the same request again, but set the value of the `pageToken` query parameter to be value of the `nextPageToken` value of the previous response. If there is no `nextPageToken` field in the response, you are on the last page.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-read`.

**OAuth2 scopes**: `api:admin-read`

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageSize` | integer | 否 | The page size to use for the endpoint. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

## Response

**ListGroupsResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListGroupsResponse` | object | 是 | 示例: `{"data":[{"name":"Data Source Admins","organizations":["ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"],"description":"Create and modify data sources in the platform","realm":"palantir-internal-realm","attributes":{"multipass:realm":["eab0a251-ca1a-4a84-a482-200edfb8026f"],"multipass:organization-rid":["ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"]},"id":"0d1fe74e-2b70-4a93-9b1a-80070637788b"}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListGroupsResponse.data` | list<Group> | 否 | — |
| `ListGroupsResponse.data.Group` | object | 是 | — |
| `ListGroupsResponse.data.Group.id` | string | 是 | A Foundry Group ID.<br>示例: `0d1fe74e-2b70-4a93-9b1a-80070637788b` |
| `ListGroupsResponse.data.Group.name` | string | 是 | The name of the Group.<br>示例: `Data Source Admins` |
| `ListGroupsResponse.data.Group.description` | string | 否 | A description of the Group.<br>示例: `Create and modify data sources in the platform` |
| `ListGroupsResponse.data.Group.realm` | string | 是 | Identifies which Realm a User or Group is a member of.<br>The `palantir-internal-realm` is used for Users or Groups that are created in Foundry by administrators and not associated with any SSO provider.<br>示例: `palantir-internal-realm` |
| `ListGroupsResponse.data.Group.organizations` | list<OrganizationRid> | 否 | The RIDs of the Organizations whose members can see this group. At least one Organization RID must be listed. |
| `ListGroupsResponse.data.Group.organizations.OrganizationRid` | string | 是 | — |
| `ListGroupsResponse.data.Group.attributes` | map | 否 | A map of the Group's attributes. Attributes prefixed with "multipass:" are reserved for internal use by Foundry and are subject to change.<br>示例: `{"multipass:realm":["eab0a251-ca1a-4a84-a482-200edfb8026f"],"multipass:organization-rid":["ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"]}` |
| `ListGroupsResponse.data.Group.attributes.AttributeName` | string | 是 | — |
| `ListGroupsResponse.data.Group.attributes.AttributeValues` | list<AttributeValue> | 是 | — |
| `ListGroupsResponse.data.Group.attributes.AttributeValues.AttributeValue` | string | 是 | — |
| `ListGroupsResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    {
      "name": "Data Source Admins",
      "organizations": [
        "ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"
      ],
      "description": "Create and modify data sources in the platform",
      "realm": "palantir-internal-realm",
      "attributes": {
        "multipass:realm": [
          "eab0a251-ca1a-4a84-a482-200edfb8026f"
        ],
        "multipass:organization-rid": [
          "ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"
        ]
      },
      "id": "0d1fe74e-2b70-4a93-9b1a-80070637788b"
    }
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `InvalidPageSize` | The provided page size was zero or negative. Page sizes must be greater than zero. |
