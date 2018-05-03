updateBadge();
var updateInterval = localStorage["update_interval"];
if (updateInterval > 0) {
  window.setInterval(function() {
    chrome.browserAction.setBadgeText({text: '...'});
    updateBadge();
  }, updateInterval * 60000);
}

function updateBadge() {
  var wallet_addr = localStorage["eth_wallet_addr"];
  var notifyIfLower = localStorage["notify_lower"];
  
  $.ajax({
    url: "https://api.ethermine.org/miner/" + wallet_addr + "/currentStats",
    method: 'get',
    success: (response) => {
      if (response && response.status=='OK') {
        // set reported hash rate
        var reportedHash = roundHash(response.data.reportedHashrate);
        chrome.browserAction.setBadgeText({text: reportedHash.toString()});
        if (reportedHash < notifyIfLower) {
          var lastNotifiedHash = localStorage["last_notified_hash"];
          var notifyInterval = localStorage["notify_interval"];
          // if newly updated hash is same as last notified, don't show again
          if (notifyInterval && notifyInterval > 0) {
            if (lastNotifiedHash && reportedHash == lastNotifiedHash) {
              var lastNotifiedTime = localStorage["last_notified_time"];
              if (lastNotifiedTime) {
                var now = new Date().getTime();
                if ((now - lastNotifiedTime) > (notifyInterval * 60000)) {
                  showNotification(reportedHash);
                }
              }
            } else {
              showNotification(reportedHash);
            }
          } else {
            showNotification(reportedHash);
          }          
        }
      }      
    }
  });
}

function showNotification(reportedHash) {
  chrome.notifications.create('', {
    type: 'basic',
    iconUrl: 'images/ethermine48.png',
    title: '[Warning] Hashrate is low',
    message: 'Total Reported Hashrate is ' + reportedHash + 'MH/s in last update.'
  }, function(notificationId) {});
  localStorage["last_notified_time"] = new Date().getTime();
  localStorage["last_notified_hash"] = reportedHash;
}

function roundHash(longHash) {
  return Math.round(longHash/10000)/100;
}