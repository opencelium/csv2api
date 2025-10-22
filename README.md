CSV to API
===========

Dynamically generate RESTful APIs from static CSVs. Provides JSON

What Problem This Solves
------------------------

The simplicity with which CSV files can be created has made them the default data format for bulk data. It is comparatively more difficult to create an API to share the same data atomically and transactionally.

What You Need
-------------

* JDK 17 or later
* * Gradle 6.8+ (optional)

How To Start
------------


1. Clone the repository:

   ```bash
   git clone https://github.com/opencelium/csv2api.git
   cd csv2api
   ```

2. Activate the systemd file and add it to the Autostart:

   ```bash
   ln -s "$(pwd)"/conf/csv2api.service /etc/systemd/system/csv2api.service
   systemctl daemon-reload
   systemctl enable csv2api
   ```

4. Start the service:

   ```bash
   systemctl start csv2api
   ```



Example Usage
---------

All examples use [data from HERE](https://people.sc.fsu.edu/~jburkardt/data/csv)

**Get CSV as JSONP (default behavior)**
http://[Csv2APIServer]:8080?source=https://people.sc.fsu.edu/~jburkardt/data/csv/deniro.csv

**Arguments**

* `source`: the URL to the source CSV

Logging
---------

The service logs can be viewed via **journalctl**:

```bash
journalctl -xe -u csv2api -o cat -f
```

This command shows the live logs (`-f`) of the `csv2api` service.

---

License
---------

CSV2API is released under the [Apache-2.0 License](LICENSE).
