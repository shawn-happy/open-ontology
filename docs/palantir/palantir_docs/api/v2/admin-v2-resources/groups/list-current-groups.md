`GET /api/v2/admin/groups/listCurrent`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Returns all Groups which contain the current user as a direct or transitive member. For example if the current user is a member of Group A and Group A is a member of Group B, this endpoint will return Group A and Group B.

Unlike the list Group Memberships endpoint which requires the `api:admin-read` scope, this endpoint
does not require any particular scopes and can be used by any authenticated user to retrieve their own
group memberships.

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**ListCurrentGroupsResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListCurrentGroupsResponse` | object | 是 | 示例: `{"data":[{"name":"Data Source Admins","organizations":["ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"],"description":"Create and modify data sources in the platform","realm":"palantir-internal-realm","attributes":{"multipass:realm":["eab0a251-ca1a-4a84-a482-200edfb8026f"],"multipass:organization-rid":["ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"]},"id":"0d1fe74e-2b70-4a93-9b1a-80070637788b"}]}` |
| `ListCurrentGroupsResponse.data` | list<Group> | 否 | — |
| `ListCurrentGroupsResponse.data.Group` | object | 是 | — |
| `ListCurrentGroupsResponse.data.Group.id` | string | 是 | A Foundry Group ID.<br>示例: `0d1fe74e-2b70-4a93-9b1a-80070637788b` |
| `ListCurrentGroupsResponse.data.Group.name` | string | 是 | The name of the Group.<br>示例: `Data Source Admins` |
| `ListCurrentGroupsResponse.data.Group.description` | string | 否 | A description of the Group.<br>示例: `Create and modify data sources in the platform` |
| `ListCurrentGroupsResponse.data.Group.realm` | string | 是 | Identifies which Realm a User or Group is a member of.<br>The `palantir-internal-realm` is used for Users or Groups that are created in Foundry by administrators and not associated with any SSO provider.<br>示例: `palantir-internal-realm` |
| `ListCurrentGroupsResponse.data.Group.organizations` | list<OrganizationRid> | 否 | The RIDs of the Organizations whose members can see this group. At least one Organization RID must be listed. |
| `ListCurrentGroupsResponse.data.Group.organizations.OrganizationRid` | string | 是 | — |
| `ListCurrentGroupsResponse.data.Group.attributes` | map | 否 | A map of the Group's attributes. Attributes prefixed with "multipass:" are reserved for internal use by Foundry and are subject to change.<br>示例: `{"multipass:realm":["eab0a251-ca1a-4a84-a482-200edfb8026f"],"multipass:organization-rid":["ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"]}` |
| `ListCurrentGroupsResponse.data.Group.attributes.AttributeName` | string | 是 | — |
| `ListCurrentGroupsResponse.data.Group.attributes.AttributeValues` | list<AttributeValue> | 是 | — |
| `ListCurrentGroupsResponse.data.Group.attributes.AttributeValues.AttributeValue` | string | 是 | — |

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
  ]
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `ListCurrentGroupsPermissionDenied` | Could not listCurrent the Group. |
