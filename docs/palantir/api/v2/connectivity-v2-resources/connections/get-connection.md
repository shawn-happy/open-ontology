`GET /api/v2/connectivity/connections/{connectionRid}`

Get the Connection with the specified rid.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:connectivity-connection-read`.

**OAuth2 scopes**: `api:connectivity-connection-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `connectionRid` | string | 是 | The Resource Identifier (RID) of a Connection (also known as a source).<br>示例: `ri.magritte..source.c078b71b-92f9-41b6-b0df-3760f411120b` |

## Response

**Connection**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Connection` | object | 是 | 示例: `{"parentFolderRid":"ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791","configuration":{"type":"jdbc","url":"jdbc:postgresql://localhost:5432/test","driverClass":"org.postgresql.Driver"},"displayName":"Connection to my external system","exportSettings":{"exportsEnabled":true,"exportEnabledWithoutMarkingsValidation":false},"rid":"ri.magritte..source.c078b71b-92f9-41b6-b0df-3760f411120b"}` |
| `Connection.rid` | string | 是 | The Resource Identifier (RID) of a Connection (also known as a source).<br>示例: `ri.magritte..source.c078b71b-92f9-41b6-b0df-3760f411120b` |
| `Connection.parentFolderRid` | string | 是 | The unique resource identifier (RID) of a Folder.<br>示例: `ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791` |
| `Connection.displayName` | string | 是 | The display name of the Connection. The display name must not be blank.<br>示例: `Connection to my external system` |
| `Connection.exportSettings` | object | 是 | The [export settings of a Connection](/docs/foundry/data-connection/export-overview/#enable-exports-for-source).<br>示例: `{"exportsEnabled":true,"exportEnabledWithoutMarkingsValidation":false}` |
| `Connection.exportSettings.exportsEnabled` | boolean | 是 | Allow exporting datasets from Foundry to this Connection.<br>示例: `true` |
| `Connection.exportSettings.exportEnabledWithoutMarkingsValidation` | boolean | 是 | In certain interactive workflows the Connection can be used in, it is not currently possible to validate the<br>security markings of the data being exported.<br>By enabling exports without markings validation, you acknowledge that you are responsible for ensuring<br>that the data being exported is compliant with your organization's policies.<br>示例: `false` |
| `Connection.worker` | union | 是 | [The worker of a Connection](/docs/foundry/data-connection/core-concepts/#workers), which defines where<br>compute for capabilities are run. |
| `Connection.worker.unknownWorker` | object | 否 | A ConnectionWorker that is not supported in the Platform APIs. This can happen because either the<br>ConnectionWorker configuration is malformed, or because the ConnectionWorker is a legacy one.<br>The ConnectionWorker should be updated to use the [Foundry worker](/docs/foundry/data-connection/core-concepts/#foundry-worker)<br>with either direct egress policies or agent proxy egress policies. |
| `Connection.worker.foundryWorker` | object | 否 | The [Foundry worker](/docs/foundry/data-connection/core-concepts/#foundry-worker) is used to run capabilities<br>in Foundry.<br>This is the preferred method for connections, as these connections benefit from Foundry's containerized<br>and scalable job execution, improved stability and do not incur the maintenance overhead associated with agents. |
| `Connection.worker.foundryWorker.networkEgressPolicyRids` | list<NetworkEgressPolicyRid> | 否 | — |
| `Connection.worker.foundryWorker.networkEgressPolicyRids.NetworkEgressPolicyRid` | string | 是 | The Resource Identifier (RID) of a Network Egress Policy. |
| `Connection.configuration` | union | 是 | 示例: `{"type":"jdbc","url":"jdbc:postgresql://localhost:5432/test","driverClass":"org.postgresql.Driver"}` |
| `Connection.configuration.s3` | object | 否 | The configuration needed to connect to an [AWS S3 external system (or any other S3-like external systems that<br>implement the s3a protocol)](/docs/foundry/available-connectors/amazon-s3/#amazon-s3). |
| `Connection.configuration.s3.bucketUrl` | string | 是 | The URL of the S3 bucket. The URL should contain a trailing slash.<br>示例: `s3://my-test-bucket/` |
| `Connection.configuration.s3.s3Endpoint` | string | 否 | The endpoint of the S3 service. This is used to connect to a custom S3 service that is not AWS S3.<br>If not specified, defaults to the [AWS S3 endpoint](https://docs.aws.amazon.com/general/latest/gr/s3.html).<br>Warning: Specifying a region and a custom endpoint containing a region can lead to unexpected behavior.<br>示例: `https://my-custom-s3-endpoint.com` |
| `Connection.configuration.s3.region` | string | 否 | The region representing the location of the S3 bucket.<br>Warning: Specifying a region and a custom endpoint containing a region can lead to unexpected behavior.<br>示例: `us-west-1` |
| `Connection.configuration.s3.authenticationMode` | union | 否 | The authentication mode to use to connect to the S3 external system. No authentication mode is required<br>to connect to publicly accessible AWS S3 buckets. |
| `Connection.configuration.s3.authenticationMode.awsAccessKey` | object | 否 | [Access keys](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_access-keys.html) are long-term<br>credentials for an IAM user or the AWS account root user.<br>Access keys consist of two parts: an access key ID (for example, AKIAIOSFODNN7EXAMPLE) and a secret access<br>key (for example, wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY). You must use both the access key ID and<br>secret access key together to authenticate your requests. |
| `Connection.configuration.s3.authenticationMode.awsAccessKey.accessKeyId` | string | 是 | 示例: `AKIAIOSFODNN7EXAMPLE` |
| `Connection.configuration.s3.authenticationMode.awsAccessKey.secretAccessKey` | union | 是 | When reading an encrypted property, the secret name representing the encrypted value will be returned.<br>When writing to an encrypted property:<br>- If a plaintext value is passed as an input, the plaintext value will be encrypted and saved to the property.<br>- If a secret name is passed as an input, the secret name must match the existing secret name of the property<br>and the property will retain its previously encrypted value.<br>示例: `{"type":"asPlaintextValue","value":"myPlaintextSecret"}` |
| `Connection.configuration.s3.authenticationMode.awsAccessKey.secretAccessKey.asSecretName` | object | 否 | — |
| `Connection.configuration.s3.authenticationMode.awsAccessKey.secretAccessKey.asSecretName.value` | string | 是 | 示例: `Password` |
| `Connection.configuration.s3.authenticationMode.awsAccessKey.secretAccessKey.asPlaintextValue` | object | 否 | — |
| `Connection.configuration.s3.authenticationMode.awsAccessKey.secretAccessKey.asPlaintextValue.value` | string | 是 | 示例: `MySecretPassword` |
| `Connection.configuration.s3.authenticationMode.cloudIdentity` | object | 否 | [Cloud identities](/docs/foundry/administration/configure-cloud-identities/) allow you to authenticate to<br>cloud provider resources without the use of static credentials. |
| `Connection.configuration.s3.authenticationMode.cloudIdentity.cloudIdentityRid` | string | 是 | The Resource Identifier (RID) of a Cloud Identity.<br>示例: `ri.resource-policy-manager.global.cloud-identity.9dfdc25e-c506-4757-96d5-d22be70d596d` |
| `Connection.configuration.s3.authenticationMode.oidc` | object | 否 | [OpenID Connect (OIDC)](/docs/foundry/data-connection/oidc/) is an open authentication protocol that allows<br>you to authenticate to external system resources without the use of static credentials. |
| `Connection.configuration.s3.authenticationMode.oidc.audience` | string | 是 | The configured audience that identifies the external system.<br>示例: `sts.amazonaws.com` |
| `Connection.configuration.s3.authenticationMode.oidc.issuerUrl` | string | 是 | The URL that identifies Foundry as an OIDC identity provider.<br>示例: `https://pltroidcpublicexample.blob.store.com/foundry` |
| `Connection.configuration.s3.authenticationMode.oidc.subject` | string | 是 | The RID of the Connection that is connecting to the external system.<br>示例: `ri.magritte..source.c078b71b-92f9-41b6-b0df-3760f411120b` |
| `Connection.configuration.s3.s3EndpointSigningRegion` | string | 否 | The region used when constructing the S3 client using a custom endpoint.<br>This is often not required and would only be needed if you are using the S3 connector with an S3-compliant third-party API,<br>and are also setting a custom endpoint that requires a non-default region.<br>示例: `us-west-1` |
| `Connection.configuration.s3.clientKmsConfiguration` | object | 否 | The client-side KMS key to use for encryption and decryption of data in the S3 bucket.<br>If not specified, the default KMS key for the bucket is used.<br>示例: `{"kmsRegion":"us-west-1","kmsKey":"arn:aws:kms:us-west-1:123456789012:key/1234abcd-12ab-34cd-56ef-1234567890ab"}` |
| `Connection.configuration.s3.clientKmsConfiguration.kmsKey` | string | 是 | The client-side KMS key to use for encryption and decryption of data in the S3 bucket.<br>If not specified, the default KMS key for the bucket is used.<br>示例: `arn:aws:kms:us-west-1:123456789012:key/1234abcd-12ab-34cd-56ef-1234567890ab` |
| `Connection.configuration.s3.clientKmsConfiguration.kmsRegion` | string | 否 | The region of the client-side KMS key to use for encryption and decryption of data in the S3 bucket.<br>If not specified, the default KMS key region for the bucket is used.<br>示例: `us-west-1` |
| `Connection.configuration.s3.stsRoleConfiguration` | object | 否 | The configuration needed to assume a role to connect to the S3 external system.<br>示例: `{"stsEndpoint":"https://sts.us-west-1.amazonaws.com","roleArn":"arn:aws:iam::123456789012:role/my-role","roleSessionDuration":{"unit":"SECONDS","value":30}}` |
| `Connection.configuration.s3.stsRoleConfiguration.roleArn` | string | 是 | The Amazon Resource Name (ARN) of the role to assume.<br>For more information, see the official [AWS documentation](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_elements_principal.html#principal-arn-format).<br>示例: `arn:aws:iam::123456789012:role/my-role` |
| `Connection.configuration.s3.stsRoleConfiguration.roleSessionName` | string | 是 | An identifier for the assumed role session.<br>The value can be any string that you assume will be unique within the AWS account.<br>For more information, see the official [AWS documentation](https://docs.aws.amazon.com/STS/latest/APIReference/API_AssumeRole.html#API_AssumeRole_RequestParameters). |
| `Connection.configuration.s3.stsRoleConfiguration.roleSessionDuration` | object | 否 | The duration of the role session.<br>The value specified can range from 900 seconds (15 minutes) up to the maximum session duration set for the role.<br>The maximum session duration setting can have a value from 1 hour to 12 hours. For more details see the official [AWS documentation](https://docs.aws.amazon.com/STS/latest/APIReference/API_AssumeRole.html#API_AssumeRole_RequestParameters).<br>示例: `{"unit":"SECONDS","value":30}` |
| `Connection.configuration.s3.stsRoleConfiguration.roleSessionDuration.value` | integer | 是 | The duration value.<br>示例: `30` |
| `Connection.configuration.s3.stsRoleConfiguration.roleSessionDuration.unit` | enum | 是 | The unit of duration.<br>示例: `SECONDS` |
| `Connection.configuration.s3.stsRoleConfiguration.externalId` | string | 否 | A unique identifier that is used by third parties when assuming roles in their customers' accounts.<br>For more information, see the official [AWS documentation](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create_for-user_externalid.html). |
| `Connection.configuration.s3.stsRoleConfiguration.stsEndpoint` | string | 否 | By default, the AWS Security Token Service (AWS STS) is available as a global service, and all AWS STS requests go to a single endpoint at https://sts.amazonaws.com.<br>AWS recommends using Regional AWS STS endpoints instead of the global endpoint to reduce latency, build in redundancy, and increase session token validity.<br>示例: `https://sts.us-west-1.amazonaws.com` |
| `Connection.configuration.s3.proxyConfiguration` | object | 否 | The configuration needed to connect to the S3 external system through a proxy.<br>示例: `{"protocol":"HTTP","credentials":{"password":{"type":"asPlaintextValue","value":"myPlaintextSecret"}}}` |
| `Connection.configuration.s3.proxyConfiguration.host` | string | 是 | Domain name, IPv4, or IPv6 address.<br>`protocol` and `port` must be specified separately. |
| `Connection.configuration.s3.proxyConfiguration.port` | integer | 是 | — |
| `Connection.configuration.s3.proxyConfiguration.nonProxyHosts` | list<string> | 否 | A list of hosts that can bypass the proxy, such as those used for STS Role. You can also use "*" wildcards. |
| `Connection.configuration.s3.proxyConfiguration.protocol` | enum | 否 | If defined, must be "HTTP" or "HTTPS". Defaults to "HTTPS".<br>示例: `HTTP` |
| `Connection.configuration.s3.proxyConfiguration.credentials` | object | 否 | 示例: `{"password":{"type":"asPlaintextValue","value":"myPlaintextSecret"}}` |
| `Connection.configuration.s3.proxyConfiguration.credentials.username` | string | 是 | — |
| `Connection.configuration.s3.proxyConfiguration.credentials.password` | union | 是 | When reading an encrypted property, the secret name representing the encrypted value will be returned.<br>When writing to an encrypted property:<br>- If a plaintext value is passed as an input, the plaintext value will be encrypted and saved to the property.<br>- If a secret name is passed as an input, the secret name must match the existing secret name of the property<br>and the property will retain its previously encrypted value.<br>示例: `{"type":"asPlaintextValue","value":"myPlaintextSecret"}` |
| `Connection.configuration.s3.proxyConfiguration.credentials.password.asSecretName` | object | 否 | — |
| `Connection.configuration.s3.proxyConfiguration.credentials.password.asSecretName.value` | string | 是 | 示例: `Password` |
| `Connection.configuration.s3.proxyConfiguration.credentials.password.asPlaintextValue` | object | 否 | — |
| `Connection.configuration.s3.proxyConfiguration.credentials.password.asPlaintextValue.value` | string | 是 | 示例: `MySecretPassword` |
| `Connection.configuration.s3.maxConnections` | integer | 否 | The maximum number of HTTP connections to the S3 service per sync.<br>If not specified, defaults to 50 as defined by the [AWS SDK default](https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/ClientConfiguration.html#DEFAULT_MAX_CONNECTIONS). |
| `Connection.configuration.s3.connectionTimeoutMillis` | string | 否 | The amount of time (in milliseconds) to wait when initially establishing a connection before giving up and timing out.<br>If not specified, defaults to 10000 as defined by the [AWS SDK default](https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/ClientConfiguration.html#DEFAULT_CONNECTION_TIMEOUT). |
| `Connection.configuration.s3.socketTimeoutMillis` | string | 否 | The amount of time (in milliseconds) to wait for data to be transferred over an established, open connection.<br>If not specified, defaults to 50000 as defined by the [AWS SDK default](https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/ClientConfiguration.html#DEFAULT_SOCKET_TIMEOUT). |
| `Connection.configuration.s3.maxErrorRetry` | integer | 否 | The maximum number of retry attempts for failed requests to the S3 service.<br>If not specified, defaults to 3 as defined by the [AWS SDK default](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/retry-strategy.html#retry-strategies). |
| `Connection.configuration.s3.matchSubfolderExactly` | boolean | 否 | If true, only files in the subfolder specified in the bucket URL will be synced.<br>If false, all files in the bucket will be synced.<br>If not specified, defaults to false. |
| `Connection.configuration.s3.enableRequesterPays` | boolean | 否 | Defaults to false, unless set and overwritten.<br>If true, includes the [requester pays header](https://docs.aws.amazon.com/AmazonS3/latest/userguide/RequesterPaysBuckets.html)<br>in requests, allowing reads from requester pays buckets. |
| `Connection.configuration.rest` | object | 否 | The configuration needed to connect to a [REST external system](/docs/foundry/available-connectors/rest-apis). |
| `Connection.configuration.rest.domains` | list<Domain> | 否 | The domains that the connection is allowed to access.<br>At least one domain must be specified. |
| `Connection.configuration.rest.domains.Domain` | object | 是 | The domain that the connection is allowed to access. |
| `Connection.configuration.rest.domains.Domain.scheme` | enum | 否 | The scheme of the domain that the connection is allowed to access.<br>If not specified, defaults to HTTPS.<br>示例: `HTTP` |
| `Connection.configuration.rest.domains.Domain.host` | string | 是 | The domain name, IPv4, or IPv6 address.<br>示例: `my-rest-api.com` |
| `Connection.configuration.rest.domains.Domain.port` | integer | 否 | The port number of the domain that the connection is allowed to access.<br>示例: `443` |
| `Connection.configuration.rest.domains.Domain.auth` | union | 否 | The URI scheme must be HTTPS if using any authentication.<br>If not specified, no authentication is required. |
| `Connection.configuration.rest.domains.Domain.auth.bearerToken` | object | 否 | The bearer token used to authenticate to the external system. |
| `Connection.configuration.rest.domains.Domain.auth.bearerToken.bearerToken` | union | 是 | When reading an encrypted property, the secret name representing the encrypted value will be returned.<br>When writing to an encrypted property:<br>- If a plaintext value is passed as an input, the plaintext value will be encrypted and saved to the property.<br>- If a secret name is passed as an input, the secret name must match the existing secret name of the property<br>and the property will retain its previously encrypted value.<br>示例: `{"type":"asPlaintextValue","value":"myPlaintextSecret"}` |
| `Connection.configuration.rest.domains.Domain.auth.bearerToken.bearerToken.asSecretName` | object | 否 | — |
| `Connection.configuration.rest.domains.Domain.auth.bearerToken.bearerToken.asSecretName.value` | string | 是 | 示例: `Password` |
| `Connection.configuration.rest.domains.Domain.auth.bearerToken.bearerToken.asPlaintextValue` | object | 否 | — |
| `Connection.configuration.rest.domains.Domain.auth.bearerToken.bearerToken.asPlaintextValue.value` | string | 是 | 示例: `MySecretPassword` |
| `Connection.configuration.rest.domains.Domain.auth.apiKey` | object | 否 | The API key used to authenticate to the external system.<br>This can be configured as a header or query parameter. |
| `Connection.configuration.rest.domains.Domain.auth.apiKey.location` | union | 是 | The location of the API key in the request. |
| `Connection.configuration.rest.domains.Domain.auth.apiKey.location.header` | object | 否 | — |
| `Connection.configuration.rest.domains.Domain.auth.apiKey.location.header.headerName` | string | 是 | The name of the header that the API key is passed in.<br>示例: `X-API-KEY` |
| `Connection.configuration.rest.domains.Domain.auth.apiKey.location.queryParameter` | object | 否 | — |
| `Connection.configuration.rest.domains.Domain.auth.apiKey.location.queryParameter.queryParameterName` | string | 是 | The name of the query parameter that the API key is passed in.<br>示例: `api_key` |
| `Connection.configuration.rest.domains.Domain.auth.apiKey.apiKey` | union | 是 | The value of the API key.<br>示例: `{"type":"asPlaintextValue","value":"myPlaintextSecret"}` |
| `Connection.configuration.rest.domains.Domain.auth.apiKey.apiKey.asSecretName` | object | 否 | — |
| `Connection.configuration.rest.domains.Domain.auth.apiKey.apiKey.asSecretName.value` | string | 是 | 示例: `Password` |
| `Connection.configuration.rest.domains.Domain.auth.apiKey.apiKey.asPlaintextValue` | object | 否 | — |
| `Connection.configuration.rest.domains.Domain.auth.apiKey.apiKey.asPlaintextValue.value` | string | 是 | 示例: `MySecretPassword` |
| `Connection.configuration.rest.domains.Domain.auth.basic` | object | 否 | — |
| `Connection.configuration.rest.domains.Domain.auth.basic.username` | string | 是 | — |
| `Connection.configuration.rest.domains.Domain.auth.basic.password` | union | 是 | When reading an encrypted property, the secret name representing the encrypted value will be returned.<br>When writing to an encrypted property:<br>- If a plaintext value is passed as an input, the plaintext value will be encrypted and saved to the property.<br>- If a secret name is passed as an input, the secret name must match the existing secret name of the property<br>and the property will retain its previously encrypted value.<br>示例: `{"type":"asPlaintextValue","value":"myPlaintextSecret"}` |
| `Connection.configuration.rest.domains.Domain.auth.basic.password.asSecretName` | object | 否 | — |
| `Connection.configuration.rest.domains.Domain.auth.basic.password.asSecretName.value` | string | 是 | 示例: `Password` |
| `Connection.configuration.rest.domains.Domain.auth.basic.password.asPlaintextValue` | object | 否 | — |
| `Connection.configuration.rest.domains.Domain.auth.basic.password.asPlaintextValue.value` | string | 是 | 示例: `MySecretPassword` |
| `Connection.configuration.rest.domains.Domain.auth.oauth2` | object | 否 | In order to use OAuth2 you must have an Outbound application configured in the [Foundry Control Panel Organization settings](/docs/foundry/administration/configure-outbound-applications#create-an-outbound-application).<br>The RID of the Outbound application must be configured in the RestConnectionConfiguration in the `oauth2ClientRid` field. |
| `Connection.configuration.rest.additionalSecrets` | union | 否 | Additional secrets that can be referenced in code and webhook configurations.<br>If not provided, no additional secrets will be created. |
| `Connection.configuration.rest.additionalSecrets.asSecretsWithPlaintextValues` | object | 否 | A map representing secret name to plaintext secret value pairs.<br>This should be used when creating or updating additional secrets for a REST connection. |
| `Connection.configuration.rest.additionalSecrets.asSecretsWithPlaintextValues.secrets` | map | 否 | The additional secrets that can be referenced in code and webhook configurations. |
| `Connection.configuration.rest.additionalSecrets.asSecretsWithPlaintextValues.secrets.SecretName` | string | 是 | — |
| `Connection.configuration.rest.additionalSecrets.asSecretsWithPlaintextValues.secrets.PlaintextValue` | string | 是 | — |
| `Connection.configuration.rest.additionalSecrets.asSecretsNames` | object | 否 | A list of secret names that can be referenced in code and webhook configurations.<br>This will be provided to the client when fetching the RestConnectionConfiguration. |
| `Connection.configuration.rest.additionalSecrets.asSecretsNames.secretNames` | list<SecretName> | 否 | The names of the additional secrets that can be referenced in code and webhook configurations. |
| `Connection.configuration.rest.additionalSecrets.asSecretsNames.secretNames.SecretName` | string | 是 | — |
| `Connection.configuration.rest.oauth2ClientRid` | string | 否 | The RID of the [Outbound application](/docs/foundry/administration/configure-outbound-applications) that is used to authenticate to the external system via OAuth2.<br>Currently, a connection may use only one outbound application for OAuth 2.0 authentication.<br>Selecting a different outbound application will update the configuration for all domains with OAuth 2.0 as the selected authorization. |
| `Connection.configuration.snowflake` | object | 否 | The configuration needed to connect to a Snowflake database. |
| `Connection.configuration.snowflake.accountIdentifier` | string | 是 | An [account identifier](https://docs.snowflake.com/en/user-guide/admin-account-identifier) uniquely<br>identifies a Snowflake account within your organization, as well as throughout the global network of<br>Snowflake-supported cloud platforms and cloud regions.<br>The URL for an account uses the following format: <account_identifier>.snowflakecomputing.com.<br>An example URL is https://acme-test_aws_us_east_2.snowflakecomputing.com.<br>示例: `acme-test_aws_us_east_2` |
| `Connection.configuration.snowflake.database` | string | 否 | Specifies the default database to use once connected. If unspecified, defaults to the empty string.<br>The specified database should be an existing database for which the specified default role has privileges.<br>See https://docs.snowflake.com/developer-guide/jdbc/jdbc-parameters#db<br>示例: `mydatabase` |
| `Connection.configuration.snowflake.role` | string | 否 | Specifies the default access control role to use in the Snowflake session initiated by the driver.<br>If unspecified, no role will be used when the session is initiated by the driver.<br>The specified role should be an existing role that has already been assigned to the specified user for<br>the driver. If the specified role has not already been assigned to the user, the role is not used when<br>the session is initiated by the driver.<br>See https://docs.snowflake.com/developer-guide/jdbc/jdbc-parameters#role<br>示例: `myrole` |
| `Connection.configuration.snowflake.schema` | string | 否 | Specifies the default schema to use for the specified database once connected. If unspecified,<br>defaults to the empty string.<br>The specified schema should be an existing schema for which the specified default role has privileges.<br>See https://docs.snowflake.com/developer-guide/jdbc/jdbc-parameters#schema<br>示例: `myschema` |
| `Connection.configuration.snowflake.warehouse` | string | 否 | Specifies the virtual warehouse to use once connected. If unspecified, defaults to the empty string.<br>The specified warehouse should be an existing warehouse for which the specified default role has privileges.<br>See https://docs.snowflake.com/developer-guide/jdbc/jdbc-parameters#warehouse<br>示例: `mywarehouse` |
| `Connection.configuration.snowflake.authenticationMode` | union | 是 | The authentication mode to use to connect to the Snowflake database. |
| `Connection.configuration.snowflake.authenticationMode.externalOauth` | object | 否 | Use an External OAuth security integration to connect and authenticate to Snowflake.<br>See https://docs.snowflake.com/en/user-guide/oauth-ext-custom |
| `Connection.configuration.snowflake.authenticationMode.externalOauth.audience` | string | 是 | Identifies the recipients that the access token is intended for as a string URI.<br>示例: `https://example.east-us-1.azure.snowflakecomputing.com` |
| `Connection.configuration.snowflake.authenticationMode.externalOauth.issuerUrl` | string | 是 | Identifies the principal that issued the access token as a string URI.<br>示例: `https://pltroidcpublicexample.blob.store.com/foundry` |
| `Connection.configuration.snowflake.authenticationMode.externalOauth.subject` | string | 是 | The RID of the Connection that is connecting to the external system.<br>示例: `ri.magritte..source.c078b71b-92f9-41b6-b0df-3760f411120b` |
| `Connection.configuration.snowflake.authenticationMode.keyPair` | object | 否 | Use a key-pair to connect and authenticate to Snowflake.<br>See https://docs.snowflake.com/en/user-guide/key-pair-auth |
| `Connection.configuration.snowflake.authenticationMode.keyPair.user` | string | 是 | — |
| `Connection.configuration.snowflake.authenticationMode.keyPair.privateKey` | union | 是 | When reading an encrypted property, the secret name representing the encrypted value will be returned.<br>When writing to an encrypted property:<br>- If a plaintext value is passed as an input, the plaintext value will be encrypted and saved to the property.<br>- If a secret name is passed as an input, the secret name must match the existing secret name of the property<br>and the property will retain its previously encrypted value.<br>示例: `{"type":"asPlaintextValue","value":"myPlaintextSecret"}` |
| `Connection.configuration.snowflake.authenticationMode.keyPair.privateKey.asSecretName` | object | 否 | — |
| `Connection.configuration.snowflake.authenticationMode.keyPair.privateKey.asSecretName.value` | string | 是 | 示例: `Password` |
| `Connection.configuration.snowflake.authenticationMode.keyPair.privateKey.asPlaintextValue` | object | 否 | — |
| `Connection.configuration.snowflake.authenticationMode.keyPair.privateKey.asPlaintextValue.value` | string | 是 | 示例: `MySecretPassword` |
| `Connection.configuration.snowflake.authenticationMode.basic` | object | 否 | — |
| `Connection.configuration.snowflake.authenticationMode.basic.username` | string | 是 | — |
| `Connection.configuration.snowflake.authenticationMode.basic.password` | union | 是 | When reading an encrypted property, the secret name representing the encrypted value will be returned.<br>When writing to an encrypted property:<br>- If a plaintext value is passed as an input, the plaintext value will be encrypted and saved to the property.<br>- If a secret name is passed as an input, the secret name must match the existing secret name of the property<br>and the property will retain its previously encrypted value.<br>示例: `{"type":"asPlaintextValue","value":"myPlaintextSecret"}` |
| `Connection.configuration.snowflake.authenticationMode.basic.password.asSecretName` | object | 否 | — |
| `Connection.configuration.snowflake.authenticationMode.basic.password.asSecretName.value` | string | 是 | 示例: `Password` |
| `Connection.configuration.snowflake.authenticationMode.basic.password.asPlaintextValue` | object | 否 | — |
| `Connection.configuration.snowflake.authenticationMode.basic.password.asPlaintextValue.value` | string | 是 | 示例: `MySecretPassword` |
| `Connection.configuration.snowflake.jdbcProperties` | map | 否 | A map of [properties](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Properties.html) passed<br>to the JDBC driver to configure behavior. Refer to the documentation of your specific connection type for additional<br>available JDBC properties to add to your connection configuration.<br>This should only contain unencrypted properties, all values specified here are sent unencrypted to Foundry.<br>示例: `{"propertyName1":"propertyValue1","propertyName2":"propertyValue2"}` |
| `Connection.configuration.databricks` | object | 否 | The configuration needed to connect to a [Databricks external system](/docs/foundry/available-connectors/databricks).<br>Refer to the [official Databricks documentation](https://docs.databricks.com/aws/en/integrations/compute-details)<br>for more information on how to obtain connection details for your system. |
| `Connection.configuration.databricks.hostName` | string | 是 | The hostname of the Databricks workspace.<br>示例: `west-us-2.azuredatabricks.net` |
| `Connection.configuration.databricks.httpPath` | string | 是 | The Databricks compute resourceâs HTTP Path value.<br>示例: `/sql/1.0/warehouses/1234` |
| `Connection.configuration.databricks.authentication` | union | 是 | The method of authentication to use. |
| `Connection.configuration.databricks.authentication.workflowIdentityFederation` | object | 否 | Authenticate as a service principal using workload identity federation. This is the recommended way to connect to Databricks.<br>Workload identity federation allows workloads running in Foundry to access Databricks APIs without the need for Databricks secrets.<br>Refer to our [OIDC documentation](/docs/foundry/data-connection/oidc) for an overview of how OpenID Connect is supported in Foundry.<br>A service principal federation policy must exist in Databricks to allow Foundry to act as an identity provider.<br>Refer to the [official documentation](https://docs.databricks.com/aws/en/dev-tools/auth/oauth-federation) for guidance. |
| `Connection.configuration.databricks.authentication.workflowIdentityFederation.servicePrincipalApplicationId` | string | 否 | The ID of the Databricks [service principal](https://docs.databricks.com/aws/en/admin/users-groups/service-principals).<br>If provided, a federated JWT token is exchanged using a<br>service principal federation policy. If not provided, a federated JWT token is exchanged using an account<br>federation policy. |
| `Connection.configuration.databricks.authentication.workflowIdentityFederation.issuerUrl` | string | 是 | Identifies the principal that issued the access token as a string URI.<br>示例: `https://pltroidcpublicexample.s3.us-east-1.amazonaws.com/foundry` |
| `Connection.configuration.databricks.authentication.workflowIdentityFederation.audience` | string | 是 | Identifies the recipients that the access token is intended for as a string URI.<br>This should be the primary host name where the Connection lives.<br>示例: `https://example.palantirfoundry.com` |
| `Connection.configuration.databricks.authentication.workflowIdentityFederation.subject` | string | 是 | The RID of the Connection that is connecting to the external system.<br>示例: `ri.magritte..source.c078b71b-92f9-41b6-b0df-3760f411120b` |
| `Connection.configuration.databricks.authentication.oauthM2M` | object | 否 | Authenticate as a service principal using OAuth. Create a service principal in Databricks and generate an OAuth secret to obtain a client ID and secret.<br>Read the [official Databricks documentation](https://docs.databricks.com/aws/en/dev-tools/auth/oauth-m2m) for more information about OAuth machine-to-machine<br>authentication. |
| `Connection.configuration.databricks.authentication.oauthM2M.clientID` | string | 是 | The client ID for the service principal. |
| `Connection.configuration.databricks.authentication.oauthM2M.clientSecret` | union | 是 | The value of the client secret.<br>示例: `{"type":"asPlaintextValue","value":"myPlaintextSecret"}` |
| `Connection.configuration.databricks.authentication.oauthM2M.clientSecret.asSecretName` | object | 否 | — |
| `Connection.configuration.databricks.authentication.oauthM2M.clientSecret.asSecretName.value` | string | 是 | 示例: `Password` |
| `Connection.configuration.databricks.authentication.oauthM2M.clientSecret.asPlaintextValue` | object | 否 | — |
| `Connection.configuration.databricks.authentication.oauthM2M.clientSecret.asPlaintextValue.value` | string | 是 | 示例: `MySecretPassword` |
| `Connection.configuration.databricks.authentication.personalAccessToken` | object | 否 | Authenticate as a user or service principal using a personal access token.<br>Read the [official Databricks documentation](https://docs.databricks.com/aws/en/dev-tools/auth/pat) for information on generating a personal access token. |
| `Connection.configuration.databricks.authentication.personalAccessToken.personalAccessToken` | union | 是 | When reading an encrypted property, the secret name representing the encrypted value will be returned.<br>When writing to an encrypted property:<br>- If a plaintext value is passed as an input, the plaintext value will be encrypted and saved to the property.<br>- If a secret name is passed as an input, the secret name must match the existing secret name of the property<br>and the property will retain its previously encrypted value.<br>示例: `{"type":"asPlaintextValue","value":"myPlaintextSecret"}` |
| `Connection.configuration.databricks.authentication.personalAccessToken.personalAccessToken.asSecretName` | object | 否 | — |
| `Connection.configuration.databricks.authentication.personalAccessToken.personalAccessToken.asSecretName.value` | string | 是 | 示例: `Password` |
| `Connection.configuration.databricks.authentication.personalAccessToken.personalAccessToken.asPlaintextValue` | object | 否 | — |
| `Connection.configuration.databricks.authentication.personalAccessToken.personalAccessToken.asPlaintextValue.value` | string | 是 | 示例: `MySecretPassword` |
| `Connection.configuration.databricks.authentication.basic` | object | 否 | — |
| `Connection.configuration.databricks.authentication.basic.username` | string | 是 | — |
| `Connection.configuration.databricks.authentication.basic.password` | union | 是 | When reading an encrypted property, the secret name representing the encrypted value will be returned.<br>When writing to an encrypted property:<br>- If a plaintext value is passed as an input, the plaintext value will be encrypted and saved to the property.<br>- If a secret name is passed as an input, the secret name must match the existing secret name of the property<br>and the property will retain its previously encrypted value.<br>示例: `{"type":"asPlaintextValue","value":"myPlaintextSecret"}` |
| `Connection.configuration.databricks.authentication.basic.password.asSecretName` | object | 否 | — |
| `Connection.configuration.databricks.authentication.basic.password.asSecretName.value` | string | 是 | 示例: `Password` |
| `Connection.configuration.databricks.authentication.basic.password.asPlaintextValue` | object | 否 | — |
| `Connection.configuration.databricks.authentication.basic.password.asPlaintextValue.value` | string | 是 | 示例: `MySecretPassword` |
| `Connection.configuration.databricks.jdbcProperties` | map | 否 | A map of [properties](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Properties.html) passed<br>to the JDBC driver to configure behavior. Refer to the documentation of your specific connection type for additional<br>available JDBC properties to add to your connection configuration.<br>This should only contain unencrypted properties, all values specified here are sent unencrypted to Foundry.<br>示例: `{"propertyName1":"propertyValue1","propertyName2":"propertyValue2"}` |
| `Connection.configuration.smb` | object | 否 | — |
| `Connection.configuration.smb.hostname` | string | 是 | Any identifier that can resolve to a server hosting an SMB share. This includes IP addresses, local<br>network names (e.g. FS-SERVER-01) or FQDNs. Should not include any protocol information like https://, smb://, etc |
| `Connection.configuration.smb.port` | integer | 否 | 445 by default |
| `Connection.configuration.smb.proxy` | object | 否 | Egress proxy to pass all traffic through.<br>示例: `{"protocol":"HTTP"}` |
| `Connection.configuration.smb.proxy.hostname` | string | 是 | — |
| `Connection.configuration.smb.proxy.port` | integer | 是 | — |
| `Connection.configuration.smb.proxy.protocol` | enum | 是 | 示例: `HTTP` |
| `Connection.configuration.smb.share` | string | 是 | Must be a valid SMB share name.<br>https://learn.microsoft.com/en-us/openspecs/windows_protocols/ms-fscc/dc9978d7-6299-4c5a-a22d-a039cdc716ea |
| `Connection.configuration.smb.baseDirectory` | string | 否 | All reads and writes in this source will happen in this subdirectory |
| `Connection.configuration.smb.auth` | union | 是 | — |
| `Connection.configuration.smb.auth.usernamePassword` | object | 否 | — |
| `Connection.configuration.smb.auth.usernamePassword.username` | string | 是 | — |
| `Connection.configuration.smb.auth.usernamePassword.password` | union | 是 | When reading an encrypted property, the secret name representing the encrypted value will be returned.<br>When writing to an encrypted property:<br>- If a plaintext value is passed as an input, the plaintext value will be encrypted and saved to the property.<br>- If a secret name is passed as an input, the secret name must match the existing secret name of the property<br>and the property will retain its previously encrypted value.<br>示例: `{"type":"asPlaintextValue","value":"myPlaintextSecret"}` |
| `Connection.configuration.smb.auth.usernamePassword.password.asSecretName` | object | 否 | — |
| `Connection.configuration.smb.auth.usernamePassword.password.asSecretName.value` | string | 是 | 示例: `Password` |
| `Connection.configuration.smb.auth.usernamePassword.password.asPlaintextValue` | object | 否 | — |
| `Connection.configuration.smb.auth.usernamePassword.password.asPlaintextValue.value` | string | 是 | 示例: `MySecretPassword` |
| `Connection.configuration.smb.auth.usernamePassword.domain` | string | 否 | Optionally specify a Windows domain to use when authenticating. Normal DNS domain restrictions apply<br>but the top-level domain might be something non-standard like .local. Defaults to WORKGROUP |
| `Connection.configuration.smb.requireMessageSigning` | boolean | 否 | If true, the client will request that the server sign all messages. If the server does not support<br>message signing, the connection will fail. Defaults to true. |
| `Connection.configuration.jdbc` | object | 否 | The configuration needed to connect to an external system using the JDBC protocol. |
| `Connection.configuration.jdbc.url` | string | 是 | The URL that the JDBC driver uses to connect to a database.<br>示例: `jdbc:postgresql://localhost:5432/test` |
| `Connection.configuration.jdbc.driverClass` | string | 是 | The fully-qualified driver class name that is used to connect to the database.<br>示例: `org.postgresql.Driver` |
| `Connection.configuration.jdbc.uploadedJdbcDrivers` | list<JdbcDriverArtifactName> | 否 | The list of uploaded JDBC driver names.<br>To upload drivers to a JDBC connection, use the uploadCustomJdbcDrivers endpoint |
| `Connection.configuration.jdbc.uploadedJdbcDrivers.JdbcDriverArtifactName` | string | 是 | The name of the uploaded JDBC artifact. |
| `Connection.configuration.jdbc.jdbcProperties` | map | 否 | A map of [properties](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Properties.html) passed<br>to the JDBC driver to configure behavior. Refer to the documentation of your specific connection type for additional<br>available JDBC properties to add to your connection configuration.<br>This should only contain unencrypted properties, all values specified here are sent unencrypted to Foundry.<br>示例: `{"propertyName1":"propertyValue1","propertyName2":"propertyValue2"}` |
| `Connection.configuration.jdbc.credentials` | object | 否 | 示例: `{"password":{"type":"asPlaintextValue","value":"myPlaintextSecret"}}` |
| `Connection.configuration.jdbc.credentials.username` | string | 是 | — |
| `Connection.configuration.jdbc.credentials.password` | union | 是 | When reading an encrypted property, the secret name representing the encrypted value will be returned.<br>When writing to an encrypted property:<br>- If a plaintext value is passed as an input, the plaintext value will be encrypted and saved to the property.<br>- If a secret name is passed as an input, the secret name must match the existing secret name of the property<br>and the property will retain its previously encrypted value.<br>示例: `{"type":"asPlaintextValue","value":"myPlaintextSecret"}` |
| `Connection.configuration.jdbc.credentials.password.asSecretName` | object | 否 | — |
| `Connection.configuration.jdbc.credentials.password.asSecretName.value` | string | 是 | 示例: `Password` |
| `Connection.configuration.jdbc.credentials.password.asPlaintextValue` | object | 否 | — |
| `Connection.configuration.jdbc.credentials.password.asPlaintextValue.value` | string | 是 | 示例: `MySecretPassword` |

```json
{
  "parentFolderRid": "ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791",
  "configuration": {
    "type": "jdbc",
    "url": "jdbc:postgresql://localhost:5432/test",
    "driverClass": "org.postgresql.Driver"
  },
  "displayName": "Connection to my external system",
  "exportSettings": {
    "exportsEnabled": true,
    "exportEnabledWithoutMarkingsValidation": false
  },
  "rid": "ri.magritte..source.c078b71b-92f9-41b6-b0df-3760f411120b"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `ParentFolderNotFoundForConnection` | The parent folder for the specified connection could not be found. |
| INVALID_ARGUMENT | `ConnectionTypeNotSupported` | The specified connection is not yet supported in the Platform API. |
| NOT_FOUND | `ConnectionNotFound` | The given Connection could not be found. |
