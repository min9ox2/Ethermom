'use strict';

$(document).ready(function() {
  loadOptions();

  $("#saveOpts").click(function() {
    saveOptions();
  });
});

function loadOptions() {
  var ethWalletAddr = localStorage["eth_wallet_addr"];
  var updateInterval = localStorage["update_interval"];
  var notifyLower = localStorage["notify_lower"];
  var notifyInterval = localStorage["notify_interval"];

  if (!updateInterval) {
    updateInterval = 3;
  }

  if (!notifyLower) {
    notifyLower = 0;
  }

  $("#eth_wallet_addr").val(ethWalletAddr);
  $("#update_interval").val(updateInterval);
  $("#notify_lower").val(notifyLower);
  $("#notify_interval").val(notifyInterval);
}

function saveOptions() {
  localStorage["eth_wallet_addr"] = $("#eth_wallet_addr").val();
  localStorage["update_interval"] = $("#update_interval").val();
  localStorage["notify_lower"] = $("#notify_lower").val();
  localStorage["notify_interval"] = $("#notify_interval").val();
  $("#saved-alert").show();
}
