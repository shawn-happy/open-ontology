`GET /api/v2/admin/users/getCurrent`

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-read`.

**OAuth2 scopes**: `api:admin-read`

## Response

**User**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `User` | object | 是 | 示例: `{"givenName":"John","familyName":"Smith","organization":"ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa","realm":"palantir-internal-realm","attributes":{"multipass:givenName":["John"],"multipass:familyName":["Smith"],"multipass:email:primary":["jsmith@example.com"],"multipass:realm":["eab0a251-ca1a-4a84-a482-200edfb8026f"],"multipass:organization-rid":["ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"],"department":["Finance"],"jobTitle":["Accountant"]},"id":"0d1fe74e-2b70-4a93-9b1a-80070637788b","email":"jsmith@example.com","username":"jsmith","status":"ACTIVE"}` |
| `User.id` | string | 是 | A Foundry User ID.<br>示例: `dd05feb8-662a-445d-8f4c-ce92d46bedeb` |
| `User.username` | string | 是 | The Foundry username of the User. This is unique within the realm.<br>示例: `jsmith` |
| `User.givenName` | string | 否 | The given name of the User.<br>示例: `John` |
| `User.familyName` | string | 否 | The family name (last name) of the User.<br>示例: `Smith` |
| `User.email` | string | 否 | The email at which to contact a User. Multiple users may have the same email address.<br>示例: `jsmith@example.com` |
| `User.realm` | string | 是 | Identifies which Realm a User or Group is a member of.<br>The `palantir-internal-realm` is used for Users or Groups that are created in Foundry by administrators and not associated with any SSO provider.<br>示例: `palantir-internal-realm` |
| `User.organization` | string | 否 | The RID of the user's primary Organization. This will be blank for third-party application service users.<br>示例: `ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa` |
| `User.status` | enum | 是 | The current status of the user.<br>示例: `ACTIVE` |
| `User.attributes` | map | 否 | A map of the User's attributes. Attributes prefixed with "multipass:" are reserved for internal use by<br>Foundry and are subject to change. Additional attributes may be configured by Foundry administrators in<br>Control Panel and populated by the User's SSO provider upon login.<br>示例: `{"multipass:givenName":["John"],"multipass:familyName":["Smith"],"multipass:email:primary":["jsmith@example.com"],"multipass:realm":["eab0a251-ca1a-4a84-a482-200edfb8026f"],"multipass:organization-rid":["ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"],"department":["Finance"],"jobTitle":["Accountant"]}` |
| `User.attributes.AttributeName` | string | 是 | — |
| `User.attributes.AttributeValues` | list<AttributeValue> | 是 | — |
| `User.attributes.AttributeValues.AttributeValue` | string | 是 | — |

```json
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
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `GetCurrentUserPermissionDenied` | Could not getCurrent the User. |
