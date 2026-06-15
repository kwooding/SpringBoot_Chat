const { Client } = require('@stomp/stompjs');
const WebSocket = require('ws');
const readline = require('readline');

const name = process.argv[2] || 'anon';

const client = new Client({
  brokerURL: 'ws://localhost:8080/chat',
  webSocketFactory: () => new WebSocket('ws://localhost:8080/chat'),
  onConnect: () => {
    console.log('--- Connected as ' + name + ' ---');

    client.subscribe('/topic/messages', msg => {
      console.log(msg.body);
    });

    const rl = readline.createInterface({ input: process.stdin });
    rl.on('line', line => {
      client.publish({
        destination: '/app/chat',
        body: JSON.stringify({ sender: name, content: line })
      });
    });
  },
  onStompError: frame => console.error('STOMP error:', frame.body)
});

client.activate();