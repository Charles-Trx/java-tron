package org.tron.common.prometheus;

import static org.tron.common.prometheus.MetricLabels.ATTACK_TYPE;

import io.prometheus.client.Counter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "metrics")
class MetricsCounter {

  private static final Map<String, Counter> container  = new ConcurrentHashMap<>();

  static {
    init(MetricKeys.Counter.TXS, "tron  txs  info .", "type", "detail");
    init(MetricKeys.Counter.MINER, "tron  miner info .", "miner", "type");
    init(MetricKeys.Counter.BLOCK_FORK, "tron  block fork info .", "type");
    init(MetricKeys.Counter.P2P_ERROR, "tron p2p error  info .", "type");
    init(MetricKeys.Counter.P2P_DISCONNECT, "tron p2p disconnect .", "type");
    init(MetricKeys.Counter.INTERNAL_SERVICE_FAIL, "internal Service fail.", "class", "method");
    init(MetricKeys.Counter.TX_ATTACK, "transaction attack.", ATTACK_TYPE);
    init(MetricKeys.Counter.BLK_ATTACK, "block attack.", ATTACK_TYPE);
  }

  private MetricsCounter() {
    throw new IllegalStateException("MetricsCounter");
  }

  private static void init(String name, String help, String... labels) {
    container.put(name, Counter.build()
        .name(name)
        .help(help)
        .labelNames(labels)
        .register());
  }

  static void inc(String key, double amt, String... labels) {
    if (Metrics.enabled()) {
      Counter counter = container.get(key);
      if (counter == null) {
        logger.info("{} not exist", key);
        return;
      }
      counter.labels(labels).inc(amt);
    }
  }
}

