`POST /api/v2/admin/groups`

Creates a new Group.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-write`.

**OAuth2 scopes**: `api:admin-write`

## Request body

```json
{
  "name": "Data Source Admins",
  "organizations": [
    "ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"
  ],
  "description": "Create and modify data sources in the platform",
  "attributes": {
    "multipass:realm": [
      "eab0a251-ca1a-4a84-a482-200edfb8026f"
    ],
    "multipass:organization-rid": [
      "ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"
    ]
  }
}
```

## Response

**Group**

The created Group

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Group` | object | 是 | The created Group<br>示例: `{"name":"Data Source Admins","organizations":["ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"],"description":"Create and modify data sources in the platform","realm":"palantir-internal-realm","attributes":{"multipass:realm":["eab0a251-ca1a-4a84-a482-200edfb8026f"],"multipass:organization-rid":["ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"]},"id":"0d1fe74e-2b70-4a93-9b1a-80070637788b"}` |
| `Group.id` | string | 是 | A Foundry Group ID.<br>示例: `0d1fe74e-2b70-4a93-9b1a-80070637788b` |
| `Group.name` | string | 是 | The name of the Group.<br>示例: `Data Source Admins` |
| `Group.description` | string | 否 | A description of the Group.<br>示例: `Create and modify data sources in the platform` |
| `Group.realm` | string | 是 | Identifies which Realm a User or Group is a member of.<br>The `palantir-internal-realm` is used for Users or Groups that are created in Foundry by administrators and not associated with any SSO provider.<br>示例: `palantir-internal-realm` |
| `Group.organizations` | list<OrganizationRid> | 否 | The RIDs of the Organizations whose members can see this group. At least one Organization RID must be listed. |
| `Group.organizations.OrganizationRid` | string | 是 | — |
| `Group.attributes` | map | 否 | A map of the Group's attributes. Attributes prefixed with "multipass:" are reserved for internal use by Foundry and are subject to change.<br>示例: `{"multipass:realm":["eab0a251-ca1a-4a84-a482-200edfb8026f"],"multipass:organization-rid":["ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"]}` |
| `Group.attributes.AttributeName` | string | 是 | — |
| `Group.attributes.AttributeValues` | list<AttributeValue> | 是 | — |
| `Group.attributes.AttributeValues.AttributeValue` | string | 是 | — |

```json
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
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `InvalidGroupOrganizations` | At least one Organization RID must be provided for a group |
| INVALID_ARGUMENT | `GroupNameAlreadyExists` | A group with this name already exists |
| PERMISSION_DENIED | `CreateGroupPermissionDenied` | Could not create the Group. |
| NOT_FOUND | `OrganizationNotFound` | The given Organization could not be found. |
