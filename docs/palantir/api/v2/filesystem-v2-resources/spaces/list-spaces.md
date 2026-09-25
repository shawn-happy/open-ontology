`GET /api/v2/filesystem/spaces`

Lists all Spaces.

This is a paged endpoint. Each page may be smaller or larger than the requested page size. However, it is guaranteed that if there are more results available, the `nextPageToken` field will be populated. To get the next page, make the same request again, but set the value of the `pageToken` query parameter to be value of the `nextPageToken` value of the previous response. If there is no `nextPageToken` field in the response, you are on the last page.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:filesystem-read`.

**OAuth2 scopes**: `api:filesystem-read`

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageSize` | integer | 否 | The page size to use for the endpoint. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

## Response

**ListSpacesResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListSpacesResponse` | object | 是 | 示例: `{"data":[{"path":"/My space-576e4","usageAccountRid":"ri.resource-policy-manager.global.usage-account.0c91194d-b5e3-4c4f-b96f-7a7f3f50e95c","fileSystemId":"hdfs","displayName":"My Space","organizations":["ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"],"description":"This space is for xyz","deletionPolicyOrganizations":["ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"],"defaultRoleSetId":"3181190f-f6b8-4649-90ec-64fa2d847204","spaceMavenIdentifier":"com.palantir.your-space","rid":"ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791"}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListSpacesResponse.data` | list<Space> | 否 | — |
| `ListSpacesResponse.data.Space` | object | 是 | — |
| `ListSpacesResponse.data.Space.rid` | string | 是 | The unique resource identifier (RID) of a Space.<br>示例: `ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791` |
| `ListSpacesResponse.data.Space.displayName` | string | 是 | The display name of the resource<br>示例: `My Space` |
| `ListSpacesResponse.data.Space.description` | string | 否 | The description of the Space.<br>示例: `This space is for xyz` |
| `ListSpacesResponse.data.Space.path` | string | 是 | The full path to the resource, including the resource name itself<br>示例: `/My space-576e4` |
| `ListSpacesResponse.data.Space.fileSystemId` | string | 是 | The ID of the Filesystem for this Space, which is where the contents of the Space are stored. If not provided, the default Filesystem for this Enrollment will be used.<br>示例: `hdfs` |
| `ListSpacesResponse.data.Space.usageAccountRid` | string | 是 | The RID of the Usage Account for this Space. Resource usage for projects in this space will accrue to this Usage Account by default. If not provided, the default Usage Account for this Enrollment will be used.<br>示例: `ri.resource-policy-manager.global.usage-account.0c91194d-b5e3-4c4f-b96f-7a7f3f50e95c` |
| `ListSpacesResponse.data.Space.organizations` | list<OrganizationRid> | 否 | The list of Organizations that are provisioned access to this Space. In order to access this Space, a user must be a member of at least one of these Organizations. |
| `ListSpacesResponse.data.Space.organizations.OrganizationRid` | string | 是 | — |
| `ListSpacesResponse.data.Space.deletionPolicyOrganizations` | list<OrganizationRid> | 否 | By default, this Space will use a Last Out deletion policy, meaning that this Space and its projects will be deleted when the last Organization listed here is deleted. Only Organizations in the Space's Enrollment can be included here. |
| `ListSpacesResponse.data.Space.deletionPolicyOrganizations.OrganizationRid` | string | 是 | — |
| `ListSpacesResponse.data.Space.defaultRoleSetId` | string | 是 | The ID of the default Role Set for this Space, which defines the set of roles that Projects in this Space must use. If not provided, the default Role Set for Projects will be used.<br>示例: `3181190f-f6b8-4649-90ec-64fa2d847204` |
| `ListSpacesResponse.data.Space.spaceMavenIdentifier` | string | 否 | The maven identifier used as the prefix to the maven coordinate that uniquely identifies resources published from this space. This is only present if configured in control panel in the space settings.<br>示例: `com.palantir.your-space` |
| `ListSpacesResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    {
      "path": "/My space-576e4",
      "usageAccountRid": "ri.resource-policy-manager.global.usage-account.0c91194d-b5e3-4c4f-b96f-7a7f3f50e95c",
      "fileSystemId": "hdfs",
      "displayName": "My Space",
      "organizations": [
        "ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"
      ],
      "description": "This space is for xyz",
      "deletionPolicyOrganizations": [
        "ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"
      ],
      "defaultRoleSetId": "3181190f-f6b8-4649-90ec-64fa2d847204",
      "spaceMavenIdentifier": "com.palantir.your-space",
      "rid": "ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791"
    }
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```
