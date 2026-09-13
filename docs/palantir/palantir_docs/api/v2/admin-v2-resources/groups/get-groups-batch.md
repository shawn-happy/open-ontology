`POST /api/v2/admin/groups/getBatch`

Execute multiple get requests on Group.

The maximum batch size for this endpoint is 500.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-read`.

**OAuth2 scopes**: `api:admin-read`

## Request body

```json
[
  {
    "groupId": "0d1fe74e-2b70-4a93-9b1a-80070637788b"
  }
]
```

## Response

**GetGroupsBatchResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `GetGroupsBatchResponse` | object | 是 | 示例: `{"data":{"0d1fe74e-2b70-4a93-9b1a-80070637788b":{"name":"Data Source Admins","organizations":["ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"],"description":"Create and modify data sources in the platform","realm":"palantir-internal-realm","attributes":{"multipass:realm":["eab0a251-ca1a-4a84-a482-200edfb8026f"],"multipass:organization-rid":["ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"]},"id":"0d1fe74e-2b70-4a93-9b1a-80070637788b"}}}` |
| `GetGroupsBatchResponse.data` | map | 否 | — |
| `GetGroupsBatchResponse.data.GroupId` | string | 是 | A Foundry Group ID. |
| `GetGroupsBatchResponse.data.Group` | object | 是 | — |
| `GetGroupsBatchResponse.data.Group.id` | string | 是 | A Foundry Group ID.<br>示例: `0d1fe74e-2b70-4a93-9b1a-80070637788b` |
| `GetGroupsBatchResponse.data.Group.name` | string | 是 | The name of the Group.<br>示例: `Data Source Admins` |
| `GetGroupsBatchResponse.data.Group.description` | string | 否 | A description of the Group.<br>示例: `Create and modify data sources in the platform` |
| `GetGroupsBatchResponse.data.Group.realm` | string | 是 | Identifies which Realm a User or Group is a member of.<br>The `palantir-internal-realm` is used for Users or Groups that are created in Foundry by administrators and not associated with any SSO provider.<br>示例: `palantir-internal-realm` |
| `GetGroupsBatchResponse.data.Group.organizations` | list<OrganizationRid> | 否 | The RIDs of the Organizations whose members can see this group. At least one Organization RID must be listed. |
| `GetGroupsBatchResponse.data.Group.organizations.OrganizationRid` | string | 是 | — |
| `GetGroupsBatchResponse.data.Group.attributes` | map | 否 | A map of the Group's attributes. Attributes prefixed with "multipass:" are reserved for internal use by Foundry and are subject to change.<br>示例: `{"multipass:realm":["eab0a251-ca1a-4a84-a482-200edfb8026f"],"multipass:organization-rid":["ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"]}` |
| `GetGroupsBatchResponse.data.Group.attributes.AttributeName` | string | 是 | — |
| `GetGroupsBatchResponse.data.Group.attributes.AttributeValues` | list<AttributeValue> | 是 | — |
| `GetGroupsBatchResponse.data.Group.attributes.AttributeValues.AttributeValue` | string | 是 | — |

```json
{
  "data": {
    "0d1fe74e-2b70-4a93-9b1a-80070637788b": {
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
  }
}
```
