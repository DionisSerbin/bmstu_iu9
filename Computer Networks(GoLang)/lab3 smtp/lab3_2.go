package main

import (
	"flag"
	"fmt"
	"os"
	"os/signal"
	"time"

	"github.com/go-ping/ping"
)

var usage = `
Usage:
    ping [-c count] [-i interval] [-t timeout] [--privileged] host
Examples:
    # ping google continuously
    ping www.google.com
    # ping google 5 times
    ping -c 5 www.google.com
    # ping google 5 times at 500ms intervals
    ping -c 5 -i 500ms www.google.com
    # ping google for 10 seconds
    ping -t 10s www.google.com
    # Send a privileged raw ICMP ping
    sudo ping --privileged www.google.com
`

func main() {
	timeout := flag.Duration("t", time.Second*60, "set timeout")
	interval := flag.Duration("i", time.Second, "set interval")
	count := flag.Int("c", 1, "set count")
	sudo := flag.Bool("sudo", false, "privileged")

	flag.Usage = func() {
		fmt.Print(usage)
	}
	flag.Parse()

	if flag.NArg() == 0 {
		flag.Usage()
		return
	}

	hostname := flag.Arg(0)

	pinger, err := ping.NewPinger(hostname)
	if err != nil {
		fmt.Println("Failed to create pinger: " + err.Error())
		return
	}

	// listen for ctrl-C signal
	c := make(chan os.Signal, 1)
	signal.Notify(c, os.Interrupt)
	go func() {
		for range c {
			pinger.Stop()
		}
	}()

	pinger.OnSend = func(pkt *ping.Packet) {
		fmt.Println()
	}

	pinger.OnRecv = func(pkt *ping.Packet) {
		fmt.Printf("%d bytes from %s: icmp_seq=%d time=%v ttl=%v\n",
			pkt.Nbytes, pkt.IPAddr, pkt.Seq, pkt.Rtt, pkt.Ttl)
	}

	pinger.OnFinish = func(stats *ping.Statistics) {
		fmt.Printf("\n--- %s ping statistics ---\n", stats.Addr)
		fmt.Printf("%d packets transmitted, %d packets received, %v%% packet loss\n",
			stats.PacketsSent, stats.PacketsRecv, stats.PacketLoss)
		fmt.Printf("round-trip min/avg/max/stddev = %v/%v/%v/%v\n",
			stats.MinRtt, stats.AvgRtt, stats.MaxRtt, stats.StdDevRtt)
	}

	pinger.Count = *count
	pinger.Interval = *interval
	pinger.Timeout = *timeout
	pinger.SetPrivileged(*sudo)

	fmt.Printf("PING %s (%s):\n", pinger.Addr(), pinger.IPAddr())
	err = pinger.Run()
	if err != nil {
		fmt.Printf("Failed to ping target host: %s", err)
	}
	stats := pinger.Statistics()
	fmt.Println(stats)
}