`GET /api/v2/admin/users`

Lists all Users.

This is a paged endpoint. Each page may be smaller or larger than the requested page size. However, it is guaranteed that if there are more results available, the `nextPageToken` field will be populated. To get the next page, make the same request again, but set the value of the `pageToken` query parameter to be value of the `nextPageToken` value of the previous response. If there is no `nextPageToken` field in the response, you are on the last page.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-read`.

**OAuth2 scopes**: `api:admin-read`

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `include` | enum | 否 | Present status of user.<br>示例: `ACTIVE` |
| `pageSize` | integer | 否 | The page size to use for the endpoint. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

## Response

**ListUsersResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListUsersResponse` | object | 是 | 示例: `{"data":[{"givenName":"John","familyName":"Smith","organization":"ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa","realm":"palantir-internal-realm","attributes":{"multipass:givenName":["John"],"multipass:familyName":["Smith"],"multipass:email:primary":["jsmith@example.com"],"multipass:realm":["eab0a251-ca1a-4a84-a482-200edfb8026f"],"multipass:organization-rid":["ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"],"department":["Finance"],"jobTitle":["Accountant"]},"id":"0d1fe74e-2b70-4a93-9b1a-80070637788b","email":"jsmith@example.com","username":"jsmith","status":"ACTIVE"}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListUsersResponse.data` | list<User> | 否 | — |
| `ListUsersResponse.data.User` | object | 是 | — |
| `ListUsersResponse.data.User.id` | string | 是 | A Foundry User ID.<br>示例: `dd05feb8-662a-445d-8f4c-ce92d46bedeb` |
| `ListUsersResponse.data.User.username` | string | 是 | The Foundry username of the User. This is unique within the realm.<br>示例: `jsmith` |
| `ListUsersResponse.data.User.givenName` | string | 否 | The given name of the User.<br>示例: `John` |
| `ListUsersResponse.data.User.familyName` | string | 否 | The family name (last name) of the User.<br>示例: `Smith` |
| `ListUsersResponse.data.User.email` | string | 否 | The email at which to contact a User. Multiple users may have the same email address.<br>示例: `jsmith@example.com` |
| `ListUsersResponse.data.User.realm` | string | 是 | Identifies which Realm a User or Group is a member of.<br>The `palantir-internal-realm` is used for Users or Groups that are created in Foundry by administrators and not associated with any SSO provider.<br>示例: `palantir-internal-realm` |
| `ListUsersResponse.data.User.organization` | string | 否 | The RID of the user's primary Organization. This will be blank for third-party application service users.<br>示例: `ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa` |
| `ListUsersResponse.data.User.status` | enum | 是 | The current status of the user.<br>示例: `ACTIVE` |
| `ListUsersResponse.data.User.attributes` | map | 否 | A map of the User's attributes. Attributes prefixed with "multipass:" are reserved for internal use by<br>Foundry and are subject to change. Additional attributes may be configured by Foundry administrators in<br>Control Panel and populated by the User's SSO provider upon login.<br>示例: `{"multipass:givenName":["John"],"multipass:familyName":["Smith"],"multipass:email:primary":["jsmith@example.com"],"multipass:realm":["eab0a251-ca1a-4a84-a482-200edfb8026f"],"multipass:organization-rid":["ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"],"department":["Finance"],"jobTitle":["Accountant"]}` |
| `ListUsersResponse.data.User.attributes.AttributeName` | string | 是 | — |
| `ListUsersResponse.data.User.attributes.AttributeValues` | list<AttributeValue> | 是 | — |
| `ListUsersResponse.data.User.attributes.AttributeValues.AttributeValue` | string | 是 | — |
| `ListUsersResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    {
      "givenName": "John",
      "familyName": "Smith",
      "organization": "ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa",
      "realm": "palantir-internal-realm",
      "attributes": {
        "multipass:givenName": [
          "John"
        ],
        "multipass:familyName": [
          "Smith"
        ],
        "multipass:email:primary": [
          "jsmith@example.com"
        ],
        "multipass:realm": [
          "eab0a251-ca1a-4a84-a482-200edfb8026f"
        ],
        "multipass:organization-rid": [
          "ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"
        ],
        "department": [
          "Finance"
        ],
        "jobTitle": [
          "Accountant"
        ]
      },
      "id": "0d1fe74e-2b70-4a93-9b1a-80070637788b",
      "email": "jsmith@example.com",
      "username": "jsmith",
      "status": "ACTIVE"
    }
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `InvalidPageSize` | The provided page size was zero or negative. Page sizes must be greater than zero. |
| INVALID_ARGUMENT | `UserDeleted` | The user is deleted. |
