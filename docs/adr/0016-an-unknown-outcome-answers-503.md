# 0016. An outcome the ledger cannot know answers 503

Date: 2026-10-09
Status: Accepted

## Context

ADR-0010 gives the door three kinds of answer: admitted, invalid,
refused. There is a fourth case it does not name. An instance sends
its write to the store and loses the store before it hears back:
the connection breaks, or the store stops answering. The write may
have committed or not, and the instance cannot tell which (F19).

Nothing at the door handles this today. `DoorProblems` turns four
of the ledger's own exceptions into answers, and none of them is the
store's. A store failure falls through to the framework's default
error answer, whose body is not Problem Details — which ADR-0010
asks of every non-success.

The definition refuses the caller's view (L2): the promise is about
the store's numbers, not what a caller reads. So this is a
convention of the door, decided here at SL-4's opening, before the
specification that depends on it.

Say: an item has 10 on hand, and reservation R holds 3 of them. The
caller consumes R. The store commits — 7 on hand, R consumed — and
the connection breaks before the instance hears so. The instance
answers with an error. The caller reads "failed" and believes R is
still open. It is not.

## Options considered

1. **Leave it as it is.** The default error answer. It breaks
   ADR-0010's rule on bodies, and it reads as "failed" for a write
   that may have landed. Rejected.
2. **`503 Service Unavailable`, as Problem Details, saying the
   outcome is unknown.** The cause is the store being out of reach,
   which is what 503 is for. A `500` stays free for our own defects,
   so a caller or an operator can tell the two apart. Chosen.
3. **`500 Internal Server Error`, with the same body.** True, but a
   `500` also means a bug in the ledger; a store outage and a
   defect would read the same. Rejected. (`504 Gateway Timeout` was
   not weighed further: the ledger is not a gateway.)
4. **Option 2, plus a limit on how long an instance waits for the
   store.** A caller would never hang on a frozen store. Rejected
   for now, for three reasons. The store's numbers are right however
   long the wait is, and waiting is fenced (W3). A limit brings the
   unknown sooner but does not remove it: an instance that gives up
   while its commit is in flight still cannot say what happened. And
   the number itself — two seconds, thirty — is a policy nothing in
   the framing gives. Adding one later changes nothing this record
   decides.

## Decision

- **When an instance loses the store mid-request, at any door, it
  answers `503 Service Unavailable`** with a Problem Details body:
  `title` "outcome unknown", `detail` saying the request may or may
  not have taken effect.
- **What a caller may do next is each door's own decision,** already
  standing:
  - an exit — send it again. ADR-0013 answers as the first did if it
    landed; if it did not, this one does the exit; if another exit
    ended the reservation meanwhile, it is refused, which tells the
    caller theirs never happened.
  - an adjustment — send it again. ADR-0012: the same assertion gets
    the same answer.
  - a reserve — sending it again may make a second hold, orphaned
    until its expiry ends it (W2, V5). It over-holds; it cannot
    oversell.
- **No limit on how long an instance waits for the store.**

## Consequences

Good: a caller is never told "failed" for a write that may have
landed; every non-success body is Problem Details again; a store
outage reads differently from a defect.

Bad: the answer carries less than the instance sometimes knows. A
store that refused the connection before anything was sent means
nothing happened, and the caller still reads "may or may not".
Nothing the promise needs depends on telling those apart. And a
caller of a frozen store waits as long as the freeze lasts; that is
W3's, and stays fenced.

Changes no export. The caller's view stays refused (L2); this adds a
fourth kind of answer to ADR-0010's three and narrows none of them.
ADR-0010 stays Accepted.
