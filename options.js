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

  if (!updateInterval) {
    updateInterval = 3;
  }

  if (!notifyLower) {
    notifyLower = 0;
  }

  $("#eth_wallet_addr").val(ethWalletAddr);
  $("#update_interval").val(updateInterval);
  $("#notify_lower").val(notifyLower);
}

function saveOptions() {
  localStorage["eth_wallet_addr"] = $("#eth_wallet_addr").val();
  localStorage["update_interval"] = $("#update_interval").val();
  localStorage["notify_lower"] = $("#notify_lower").val();
  $("#saved-alert").show();
}
