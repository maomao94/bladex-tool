package org.springblade.core.prometheus.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * model details see https://www.consul.io/api/catalog.html#serviceport
 *
 * @author consul
 */
@Getter
@Builder
public class Service {

	@JsonProperty("Address")
	private String address;

	@JsonProperty("Node")
	private String node;

	@JsonProperty("ServiceAddress")
	private String serviceAddress;

	@JsonProperty("ServiceName")
	private String serviceName;

	@JsonProperty("ServiceID")
	private String serviceId;

	@JsonProperty("ServicePort")
	private int servicePort;

	@JsonProperty("NodeMeta")
	private Map<String, String> nodeMeta;

	@JsonProperty("ServiceMeta")
	private Map<String, String> serviceMeta;

	/**
	 * will be empty, eureka does not have the concept of service tags
	 */
	@JsonProperty("ServiceTags")
	private List<String> serviceTags;

}
