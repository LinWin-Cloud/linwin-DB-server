#
# Author: Linwincloud & zmh-program
# Description: Mydb Shell for Linwin Data Server
# Usage: Operate the Linwin Database.
#

import sys
from time import time
from drive import driver


# 因为控制台输出时序问题，暂不用logging


def _save_qps_count(bench: float) -> str:
    return f'{round(1 / bench, 2) if bench != 0 else "*"} qps'


def _get_query_params(bench: float) -> str:
    return "Query executed completely! (%0.3f sec, %s)" % (bench, _save_qps_count(bench))


if __name__ == "__main__":
    _driver = driver.Driver(
        host=input("Logon Host[default 127.0.0.1]: ").strip() or "127.0.0.1",
        port=int(input("Logon Port[default 8888]: ").strip() or 8888),
        username=input("Logon User[default root]: ").strip() or "root",
        password=input("Logon Password[default 123456]: ").strip() or "123456",
    )

    print(" Linwin DB Client ".center(64, "="))
    while True:
        script = input("LinwinDB-Mydb Shell $ ").strip()
        if script == "quit":
            sys.exit(0)
        elif not script:
            continue
        mark = time()
        try:
            response = _driver.execute(script)
        except driver.DatabaseError:
            benchmark = time() - mark
            print(_get_query_params(benchmark))
        else:
            print(response, end="")
            if response not in ("Error Command and Script\r\n", "Command syntax error!\r\n"):
                benchmark = time() - mark
                print(_get_query_params(benchmark))
                continue
            print(end="\n")
