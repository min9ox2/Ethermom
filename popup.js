$(document).ready(function() {
  var wallet_addr = localStorage["eth_wallet_addr"];

  updateAll();

  $("#refresh").click(function() {    
    updateAll();
  });

  $("#settings").click(function() {    
    chrome.runtime.openOptionsPage();
  });

  $("#goto-web").click(function() {
    if (wallet_addr) {
      chrome.tabs.create({ url: 'https://ethermine.org/miners/' + wallet_addr + '/dashboard' });
    } else {
      chrome.runtime.openOptionsPage();
    }
  })

  $(".fa-cog").mouseover(function() {
    $(".fa-cog").addClass('fa-spin');
  }).mouseout(function() {
    $(".fa-cog").removeClass('fa-spin');
  });

  $(".fa-globe").mouseover(function() {
    $(".fa-globe").addClass('fa-spin');
  }).mouseout(function() {
    $(".fa-globe").removeClass('fa-spin');
  });
  
  function updateAll() {
    $(".fa-refresh").addClass('fa-spin');
    updateMiner();
    updateWorkers();
  }
  
  function updateMiner() {
    $.ajax({
      url: "https://api.ethermine.org/miner/" + wallet_addr + "/currentStats",
      method: 'get',
      success: (response) => {
        if (response && response.status=='OK') {
          // set Time
          $("#time").text(convertToDate(response.data.time));
          // set last seen
          $("#lastseen").text(convertToDate(response.data.lastSeen));
          // set reported hash rate
          var reportedHash = roundHash(response.data.reportedHashrate);
          $("#reported-hash").text(reportedHash);
          chrome.browserAction.setBadgeText({text: reportedHash.toString()});
          // set current hash rate
          $("#current-hash").text(roundHash(response.data.currentHashrate));
          // set active workers
          $("#active-workers").text(response.data.activeWorkers);
        }
      }
    });
  }

  function updateWorkers() {
    $.ajax({
      url: "https://api.ethermine.org/miner/" + wallet_addr + "/workers",
      method: 'get',
      success: (response) => {
        if (response && response.status=='OK') {
          var workers = response.data;
          workers.forEach(element => {
            var lastSeen = '';
            if (element.lastSeen) {
              lastSeen = getTimeDiff(element.lastSeen) + 'ago';
            }
            var workerItem = '<tr>'+
                                '<td>'+ element.worker +'</td>'+
                                '<td>'+ roundHash(element.reportedHashrate) +' MH/s</td>'+
                                '<td>'+ roundHash(element.currentHashrate) +' MH/s</td>'+
                                '<td>'+ lastSeen +'</td>'+
                              '</tr>';
            $('#workers tr:last').after(workerItem);
          });
        }

        $(".fa-refresh").removeClass("fa-spin");
      }
    });
  }

  function convertToDate(unixTimestamp) {
    return new Date(unixTimestamp*1000).toLocaleTimeString('en-US')
  }

  function roundHash(longHash) {
    return Math.round(longHash/10000)/100;
  }

  function getTimeDiff(unixTimestamp) {
    var nowDate = new Date();
    var thenDate = new Date(unixTimestamp*1000);
    var seconds = Math.floor((nowDate - (thenDate))/1000);
    var minutes = Math.floor(seconds/60);
    var hours = Math.floor(minutes/60);
    var days = Math.floor(hours/24);

    hours = hours-(days*24);
    minutes = minutes-(days*24*60)-(hours*60);
    seconds = seconds-(days*24*60*60)-(hours*60*60)-(minutes*60);
    return hours ? hours + ' hr ' : ' ' + minutes ? minutes + ' mins ' : ' ' + seconds ? seconds + ' sec' : ' ';
  }
});