FROM postgres:17

ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres
ENV POSTGRES_DB=expense_tracker

CMD ["postgres", "-c", "log_statement=all", "-c", "log_destination=stderr"]
